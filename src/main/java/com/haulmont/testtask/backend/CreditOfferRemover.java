package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.CreditOfferRepository;
import com.haulmont.testtask.model.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import static com.haulmont.testtask.Setting.CREDIT_OFFER_DOES_NOT_EXIST;
import static com.haulmont.testtask.Setting.PAYMENT_PERIOD_IS_ALREADY_START_AND_DOES_NOT_END;

@AllArgsConstructor
@Transactional
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreditOfferRemover implements Remover {
    private final CreditOfferRepository creditOfferRepository;
    private final PaymentRepository paymentRepository;


    /**
     * trying to make client {@link CreditOffer#isCanceled()}
     */
    public Result<Boolean> remove(CreditOffer creditOffer) {
        try {
            if (creditOffer == null || creditOffer.getCreditOfferId() == null || creditOfferRepository.findById(creditOffer.getCreditOfferId()).isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(CREDIT_OFFER_DOES_NOT_EXIST);

            LocalDate now = LocalDate.now();
            boolean isExistsEarlier = false;
            boolean isExistsLater = false;
            for (Payment payment : creditOffer.getPayments()) {
                if (payment.getDate().compareTo(now) < 0) isExistsEarlier = true;
                if (payment.getDate().compareTo(now) > 0) isExistsLater = true;
                if (isExistsEarlier && isExistsLater)
                    throw new IllegalArgumentExceptionWithoutStackTrace(PAYMENT_PERIOD_IS_ALREADY_START_AND_DOES_NOT_END);
            }
            paymentRepository.deleteAll(creditOffer.getPayments());
            creditOffer.cancel();
            creditOffer.setPayments(Collections.emptyList());
            creditOfferRepository.save(creditOffer);
        } catch (Exception ex) {
            return Result.failure(ex);
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> remove(Removable removable) {
        return remove((CreditOffer) removable);
    }
}
