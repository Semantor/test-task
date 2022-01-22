package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.model.entity.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = App.class)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    @Test
    void findAllByRemoved() {
        List<Client> byIsRemoved = clientRepository.findByIsRemoved(true);
        Assertions.assertFalse(byIsRemoved.isEmpty());
    }

    @Test
    void testFindAllByRemoved() {
        PageRequest lastname = PageRequest.of(0, 2, Sort.by("lastName").descending());
        Page<Client> byIsRemoved = clientRepository.findByIsRemoved(true, lastname);
        Assertions.assertEquals(2,byIsRemoved.getSize());
        Assertions.assertEquals("Daelin",byIsRemoved.getContent().get(0).getFirstName());
    }

}