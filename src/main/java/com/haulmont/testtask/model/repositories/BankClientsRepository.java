package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Client;

import java.util.Set;

public interface BankClientsRepository {
    Set<Client> getBankClients(Bank bank);
}
