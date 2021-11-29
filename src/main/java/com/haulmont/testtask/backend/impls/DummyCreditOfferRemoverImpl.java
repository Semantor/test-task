package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditOfferRemover;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Removable;

public class DummyCreditOfferRemoverImpl implements CreditOfferRemover {
    @Override
    public boolean remove(CreditOffer creditOffer) {
        return false;
    }

    @Override
    public boolean remove(Removable removable) {
        return remove((CreditOffer) removable);
    }
}
