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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class CreditOfferCreatorWithExceptionImpl implements CreditOfferCreator {

    private final CreditOfferRepository creditOfferRepository;
    private final CreditRepository creditRepository;
    private final ClientRepository clientRepository;
    private final PaymentCalculator paymentCalculator;
    private final Validator validator;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(@NotNull CreditOffer creditOffer) {
        if (creditOffer.getCreditOfferId() == null)
            throw new CreateCreditOfferException("credit offer has empty id ");
        if (creditOffer.getClient() == null || creditOffer.getClient().getClientId() == null)
            throw new CreateCreditOfferException("unidentified client");
        Optional<Client> clientOpt = clientRepository.findById(creditOffer.getClient().getClientId());
        if (clientOpt.isEmpty()) throw new CreateCreditOfferException("this client does not present in db ");
        creditOffer.setClient(clientOpt.get());
        if (creditOffer.getCredit() == null || creditOffer.getCredit().getCreditId() == null)
            throw new CreateCreditOfferException("unidentified credit");
        Optional<Credit> creditOpt = creditRepository.findById(creditOffer.getCredit().getCreditId());
        if (creditOpt.isEmpty()) throw new CreateCreditOfferException("this credit does not present in db ");
        if (creditOffer.getCreditAmount() == null ||
                !validator.validateCreditAmount(creditOffer.getCreditAmount(), creditOpt.get().getCreditLimit()))
            throw new CreateCreditOfferException("not valid credit amount");
        creditOffer.setCredit(creditOpt.get());
        if (creditOffer.getMonthCount() < 1) throw new CreateCreditOfferException("not valid month count");
        if (creditOffer.getPayments() == null || creditOffer.getPayments().isEmpty())
            throw new CreateCreditOfferException("empty payment list");
        boolean b = false;
        for (Payment payment : creditOffer.getPayments()) {
            if (!payment.getCreditOffer().equals(creditOffer)) {
                b = true;
                break;
            }
        }
        if (b) {
            log.warn("payments is not connected to credit offer");
            throw new CreateCreditOfferException("payments is not connected to credit offer");
        }

        List<Payment> calculatePayments = paymentCalculator
                .calculate(creditOffer.getCredit(), creditOffer, creditOffer.getMonthCount(),
                        creditOffer.getCreditAmount(), creditOffer.getPayments().get(0).getDate());

        if (calculatePayments.size() != creditOffer.getPayments().size())
            throw new CreateCreditOfferException("wrong payments count");

        for (int i = 0; i < creditOffer.getPayments().size(); i++) {
            if (!calculatePayments.get(i).getDate().equals(creditOffer.getPayments().get(i).getDate()))
                throw new CreateCreditOfferException("wrong payments date: must be:" + calculatePayments.get(i).getDate() + ", but found: " +
                        creditOffer.getPayments().get(i).getDate());
            if (!calculatePayments.get(i).getAmount().equals(creditOffer.getPayments().get(i).getAmount()))
                throw new CreateCreditOfferException("wrong payments amount: must be:" + calculatePayments.get(i).getAmount() + ", but found:" +
                        creditOffer.getPayments().get(i).getAmount());
            if (!calculatePayments.get(i).getMainPart().equals(creditOffer.getPayments().get(i).getMainPart()))
                throw new CreateCreditOfferException("wrong payments main part: must be:" + calculatePayments.get(i).getMainPart() + ", but found:" +
                        creditOffer.getPayments().get(i).getMainPart());
            if (!calculatePayments.get(i).getPercentPart().equals(creditOffer.getPayments().get(i).getPercentPart()))
                throw new CreateCreditOfferException("wrong payments percent part: must be:" + calculatePayments.get(i).getPercentPart() + ", but found:" +
                        creditOffer.getPayments().get(i).getPercentPart());
        }

        log.info("save new credit offer: " + creditOffer);
        List<Payment> payment = creditOffer.getPayments();
        creditOffer.setPayments(Collections.emptyList());
        creditOfferRepository.save(creditOffer);
        creditOffer.setPayments(payment);
        paymentRepository.saveAll(creditOffer.getPayments());
        System.out.println(creditOffer.toSql());
    }
}
