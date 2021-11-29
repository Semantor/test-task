package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfig;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {TestConfig.class, App.class, Config.class})
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