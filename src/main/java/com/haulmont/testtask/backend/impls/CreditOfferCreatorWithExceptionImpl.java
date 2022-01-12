package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditOfferCreator;
import com.haulmont.testtask.backend.PaymentCalculator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.backend.excs.CreateCreditOfferException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.haulmont.testtask.Setting.*;

@AllArgsConstructor
@Slf4j
@Component
public class CreditOfferCreatorWithExceptionImpl implements CreditOfferCreator {

    private final CreditOfferRepository creditOfferRepository;
    private final CreditRepository creditRepository;
    private final ClientRepository clientRepository;
    private final PaymentCalculator paymentCalculator;
    private final Validator validator;
    private final PaymentRepository paymentRepository;
    public static final String SAVE_NEW_CREDIT_OFFER = "save new credit offer: ";
    public static final String PAYMENT_DATE_FIELD_NAME = "date";
    public static final String PAYMENT_MAIN_PART_FIELD_NAME = "main part";
    public static final String PAYMENT_PERCENT_PART_FIELD_NAME = "percent part";
    public static final String PAYMENT_AMOUNT_FIELD_NAME = "amount";

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(@NotNull CreditOffer creditOffer) {
        checkCreditOfferForCorrectnessClient(creditOffer);
        checkCreditOfferForCorrectnessCredit(creditOffer);

        if (creditOffer.getMonthCount() < 1)
            throw new CreateCreditOfferException(NOT_VALID_MONTH_COUNT);
        if (creditOffer.getPayments() == null || creditOffer.getPayments().isEmpty())
            throw new CreateCreditOfferException(EMPTY_PAYMENT_LIST);

        checkCreditOfferThatPaymentsHasCorrectCreditOffer(creditOffer);

        checkCreditOfferForCorrectnessPayments(creditOffer);

        log.info(SAVE_NEW_CREDIT_OFFER + creditOffer);
        List<Payment> payment = creditOffer.getPayments();
        creditOffer.setPayments(Collections.emptyList());
        creditOfferRepository.save(creditOffer);
        creditOffer.setPayments(payment);
        paymentRepository.saveAll(creditOffer.getPayments());
    }

    private void checkCreditOfferThatPaymentsHasCorrectCreditOffer(@NotNull CreditOffer creditOffer) {
        boolean isSomePaymentsHasIncorrectCreditOffer = false;
        for (Payment payment : creditOffer.getPayments()) {
            if (!payment.getCreditOffer().equals(creditOffer)) {
                isSomePaymentsHasIncorrectCreditOffer = true;
                break;
            }
        }

        if (isSomePaymentsHasIncorrectCreditOffer) {
            throw new CreateCreditOfferException(PAYMENTS_IS_NOT_CONNECTED_TO_CREDIT_OFFER);
        }
    }

    private void checkCreditOfferForCorrectnessClient(@NotNull CreditOffer creditOffer) {
        if (creditOffer.getCreditOfferId() == null)
            throw new CreateCreditOfferException(NULLABLE_ID);
        if (creditOffer.getClient() == null || creditOffer.getClient().getClientId() == null)
            throw new CreateCreditOfferException(UNIDENTIFIED_CLIENT);
        Optional<Client> clientOpt = clientRepository.findById(creditOffer.getClient().getClientId());
        if (clientOpt.isEmpty()) throw new CreateCreditOfferException(CLIENT_DOES_NOT_PRESENT_IN_DB);
    }

    private void checkCreditOfferForCorrectnessCredit(@NotNull CreditOffer creditOffer) {
        if (creditOffer.getCredit() == null || creditOffer.getCredit().getCreditId() == null)
            throw new CreateCreditOfferException(UNIDENTIFIED_CREDIT);
        Optional<Credit> creditOpt = creditRepository.findById(creditOffer.getCredit().getCreditId());
        if (creditOpt.isEmpty()) throw new CreateCreditOfferException(CREDIT_DOES_NOT_PRESENT_IN_DB);
        if (creditOffer.getCreditAmount() == null ||
                !validator.validateCreditAmount(creditOffer.getCreditAmount(), creditOpt.get().getCreditLimit()))
            throw new CreateCreditOfferException(NOT_VALID_CREDIT_AMOUNT);
    }

    private void checkCreditOfferForCorrectnessPayments(@NotNull CreditOffer creditOffer) {
        List<Payment> calculatePayments = paymentCalculator
                .calculate(creditOffer.getCredit(), creditOffer, creditOffer.getMonthCount(),
                        creditOffer.getCreditAmount(), creditOffer.getPayments().get(0).getDate().minusMonths(1));

        if (calculatePayments.size() != creditOffer.getPayments().size())
            throw new CreateCreditOfferException(WRONG_PAYMENTS_COUNT);

        for (int i = 0; i < creditOffer.getPayments().size(); i++) {
            if (!calculatePayments.get(i).getDate().equals(creditOffer.getPayments().get(i).getDate()))
                throw new CreateCreditOfferException(
                        errorMessageInPaymentList(PAYMENT_DATE_FIELD_NAME,
                                calculatePayments.get(i).getDate(),
                                creditOffer.getPayments().get(i).getDate()));

            if (!calculatePayments.get(i).getAmount().equals(creditOffer.getPayments().get(i).getAmount()))
                throw new CreateCreditOfferException(
                        errorMessageInPaymentList(PAYMENT_AMOUNT_FIELD_NAME,
                                calculatePayments.get(i).getAmount(),
                                creditOffer.getPayments().get(i).getAmount()));

            if (!calculatePayments.get(i).getMainPart().equals(creditOffer.getPayments().get(i).getMainPart()))
                throw new CreateCreditOfferException(
                        errorMessageInPaymentList(PAYMENT_MAIN_PART_FIELD_NAME,
                                calculatePayments.get(i).getMainPart(),
                                creditOffer.getPayments().get(i).getMainPart()));

            if (!calculatePayments.get(i).getPercentPart().equals(creditOffer.getPayments().get(i).getPercentPart()))
                throw new CreateCreditOfferException(
                        errorMessageInPaymentList(PAYMENT_PERCENT_PART_FIELD_NAME,
                                calculatePayments.get(i).getPercentPart(),
                                creditOffer.getPayments().get(i).getPercentPart())
                );
        }
    }

    @NotNull
    private String errorMessageInPaymentList(String fieldName, @NotNull Object expected, @NotNull Object actual) {
        return "wrong payments " + fieldName + ": must be:" + expected + ", but found: " +
                actual;
    }
}
