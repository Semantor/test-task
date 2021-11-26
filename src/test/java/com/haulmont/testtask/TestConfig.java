package com.haulmont.testtask;

import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.backend.impls.ClientSaverWithExceptionImpl;
import com.haulmont.testtask.model.repositories.ClientRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {App.class, Config.class})
@EntityScan("com.haulmont.testtask.model.entity")
public class TestConfig {
    @Bean
    ClientSaverWithExceptionImpl clientSaverWithException(ClientFieldsValidator clientFieldsValidator,
                                                          ClientRepository clientRepository,
                                                          Validator validator) {
        return new ClientSaverWithExceptionImpl(clientFieldsValidator, clientRepository, validator);
    }
}
