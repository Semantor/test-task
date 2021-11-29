package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientRemover;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Removable;

public class DummyClientRemoverImpl implements ClientRemover {
    @Override
    public boolean remove(Client client) {
        System.out.println("this remover is working" + this.getClass());
        return false;
    }

    @Override
    public boolean remove(Removable removable) {
        return remove((Client) removable);
    }
}
