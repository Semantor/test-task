package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfig;
import com.haulmont.testtask.model.entity.Credit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {TestConfig.class, App.class, Config.class})
class CreditRepositoryTest {
    @Autowired
    private CreditRepository creditRepository;

    @Test
    void findByIsUnused() {
        List<Credit> byIsUnused = creditRepository.findByIsUnused(true);
        byIsUnused.forEach(System.out::println);
        assertEquals(3,byIsUnused.size());
    }
}