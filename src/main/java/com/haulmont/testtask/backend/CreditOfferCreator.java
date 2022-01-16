package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.CreditOfferRepository;
import com.haulmont.testtask.model.repositories.CreditRepository;
import com.haulmont.testtask.model.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.haulmont.testtask.Setting.*;

@AllArgsConstructor
@Slf4j
@Component
public class CreditOfferCreator {
    private final CreditOfferRepository creditOfferRepository;
    private final CreditRepository creditRepository;
    private final ClientRepository clientRepository;
    private final PaymentCalculatorWithPercentPartDependOnRemainingAndDayCountInPeriod paymentCalculatorWithPercentPartDependOnRemainingAndDayCountInPeriod;
    private final javax.validation.Validator validator;
    private final PaymentRepository paymentRepository;

    /**
     * this method validate:
     * UUID must be not null and not present in db.
     * credit must be not null and present in db.
     * same for client.
     * credit amount must be less than {@link Credit#getCreditLimit()}
     * and more than {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_LIMIT_MIN_VALUE}
     * <p>
     * {@link CreditOffer#getPayments()} must be:
     * present
     * connect to credit offer
     * first payment with date >= today
     * <p>
     * all the payment will calculate into this method and compare with {@link CreditOffer#getPayments()}
     * if any fields differ, failed
     * <p>
     * this method also save this payment list.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Result<Boolean> save(@NotNull CreditOffer creditOffer) {

        Set<ConstraintViolation<CreditOffer>> creditOfferConstraintViolation = validator.validate(creditOffer);
        if (!creditOfferConstraintViolation.isEmpty())
            return Result.failure(
                    new IllegalArgumentExceptionWithoutStackTrace(
                            ConstraintViolationHandler.handleToString(creditOfferConstraintViolation)));
        Optional<Credit> optionalCreditFromDatabase = creditRepository.findById(creditOffer.getCredit().getCreditId());
        if (optionalCreditFromDatabase.isEmpty() || optionalCreditFromDatabase.get().isUnused())
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                    NO_VALID_CREDIT));
        Optional<Client> optionalClientFromDatabase = clientRepository.findById(creditOffer.getClient().getClientId());
        if (optionalClientFromDatabase.isEmpty() || optionalClientFromDatabase.get().isRemoved())
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                    NO_VALID_CLIENT));

        Result<Boolean> correctCreditOfferInPayment = checkCreditOfferThatPaymentsHasCorrectCreditOffer(creditOffer);
        if (correctCreditOfferInPayment.isFailure())
            return correctCreditOfferInPayment;

        Result<Boolean> correctnessPayments = checkCreditOfferForCorrectnessPayments(creditOffer);
        if (correctnessPayments.isFailure())
            return correctnessPayments;

        List<Payment> payments = creditOffer.getPayments();

        try {
            creditOffer.setPayments(Collections.emptyList());
            creditOfferRepository.save(creditOffer);
            creditOffer.setPayments(payments);
            paymentRepository.saveAll(payments);
        } catch (Exception ex) {
            return Result.failure(ex);
        } finally {
            creditOffer.setPayments(payments);
        }
        return Result.success(true);
    }

    private Result<Boolean> checkCreditOfferThatPaymentsHasCorrectCreditOffer(@NotNull CreditOffer creditOffer) {
        boolean isSomePaymentsHasIncorrectCreditOffer = false;
        for (Payment payment : creditOffer.getPayments()) {
            if (!payment.getCreditOffer().equals(creditOffer)) {
                isSomePaymentsHasIncorrectCreditOffer = true;
                break;
            }
        }

        if (isSomePaymentsHasIncorrectCreditOffer) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(PAYMENTS_IS_NOT_CONNECTED_TO_CREDIT_OFFER));
        }
        return Result.success(true);
    }

    private Result<Boolean> checkCreditOfferForCorrectnessPayments(@NotNull CreditOffer creditOffer) {
        List<Payment> calculatePayments = paymentCalculatorWithPercentPartDependOnRemainingAndDayCountInPeriod
                .calculate(creditOffer.getCredit(), creditOffer, creditOffer.getMonthCount(),
                        creditOffer.getCreditAmount(), creditOffer.getPayments().get(0).getDate().minusMonths(1));

        if (calculatePayments.size() != creditOffer.getPayments().size())
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(WRONG_PAYMENTS_COUNT));


        for (int i = 0; i < creditOffer.getPayments().size(); i++) {
            if (!calculatePayments.get(i).getDate().equals(creditOffer.getPayments().get(i).getDate()))
                return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                        errorMessageInPaymentList(DATE_LABEL,
                                calculatePayments.get(i).getDate(),
                                creditOffer.getPayments().get(i).getDate())));


            if (!calculatePayments.get(i).getAmount().equals(creditOffer.getPayments().get(i).getAmount()))
                return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                        errorMessageInPaymentList(AMOUNT_LABEL,
                                calculatePayments.get(i).getAmount(),
                                creditOffer.getPayments().get(i).getAmount())));


            if (!calculatePayments.get(i).getMainPart().equals(creditOffer.getPayments().get(i).getMainPart()))
                return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                        errorMessageInPaymentList(MAIN_PART_LABEL,
                                calculatePayments.get(i).getMainPart(),
                                creditOffer.getPayments().get(i).getMainPart())));


            if (!calculatePayments.get(i).getPercentPart().equals(creditOffer.getPayments().get(i).getPercentPart()))
                return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                        errorMessageInPaymentList(PERCENT_PART_LABEL,
                                calculatePayments.get(i).getPercentPart(),
                                creditOffer.getPayments().get(i).getPercentPart())));

        }
        return Result.success(true);
    }

    @NotNull
    private String errorMessageInPaymentList(String fieldName, @NotNull Object expected, @NotNull Object actual) {
        return "wrong payments " + fieldName + ": must be:" + expected + ", but found: " +
                actual;
    }
}
