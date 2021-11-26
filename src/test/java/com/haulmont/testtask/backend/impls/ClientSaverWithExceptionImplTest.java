package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfig;
import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {TestConfig.class, App.class, Config.class})
class ClientSaverWithExceptionImplTest {
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private ClientFieldsValidator clientFieldsValidator;

    @Autowired
    private ClientSaverWithExceptionImpl clientSaver;

    private final Client client = Client.builder()
            .lastName("Ivanov")
            .firstName("Ivan")
            .patronymic("Ivanovich")
            .email("ivan@mail.com")
            .phoneNumber("9876543210")
            .passport("6321789456")
            .build();

    @MockBean
    private Client input;


    @Test
    void saveWithNullUUID() {
        Mockito.when(input.getClientId()).then(null);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(input));
        assertEquals(CreateClientException.EMPTY_UUID, createClientException.getMessage());

    }

    @Test
    void saveWithUserUUIDPresentInDatabase() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.of(client));
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(input));
        assertEquals(CreateClientException.UUID_IS_ALREADY_USED, createClientException.getMessage());

    }

    @Test
    void saveWithUserNullName() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> null);
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(input));
        assertEquals(CreateClientException.EMPTY_NAME, createClientException.getMessage());

    }

    @Test
    void saveWithUserEmptyName() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> "");
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(input));
        assertEquals(CreateClientException.EMPTY_NAME, createClientException.getMessage());

    }

    @Test
    void saveWithUserShortName() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> "As");
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(input));
        assertEquals(CreateClientException.TOO_SHORT_NAME, createClientException.getMessage());

    }

    @Test
    void saveWithUserIncorrectSymbolsName() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> "@ASdw");
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.NAME_INCORRECT_SYMBOLS, createClientException.getMessage());
    }

    @Test
    void saveWithUserNullLastname() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> null);
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.EMPTY_LASTNAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserEmptyLastname() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> "");
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.EMPTY_LASTNAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserTooShortLastname() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> "as");
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.TOO_SHORT_LASTNAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserIncorrectSymbolsLastname() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> "@asdf");
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> client.getPhoneNumber());
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.LASTNAME_INCORRECT_SYMBOLS, createClientException.getMessage());
    }

    @Test
    void saveWithUserNullPhone() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> null);
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.NO_VALID_PHONE, createClientException.getMessage());
    }

    @Test
    void saveWithUserEmptyPhone() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> "");
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.NO_VALID_PHONE, createClientException.getMessage());
    }

    @Test
    void saveWithUserTooShortPhone() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> "987");
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.NO_VALID_PHONE, createClientException.getMessage());
    }

    @Test
    void saveWithUserWithNoDigitsPhone() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> "987654123&");
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.NO_VALID_PHONE, createClientException.getMessage());
    }

    @Test
    void saveWithUserWithStartWithNotNinePhone() {
        UUID uuid = client.getClientId();
        Mockito.when(input.getClientId()).then(invocationOnMock -> uuid);
        Mockito.when(input.getLastName()).then(invocationOnMock -> client.getLastName());
        Mockito.when(input.getFirstName()).then(invocationOnMock -> client.getFirstName());
        Mockito.when(input.getPatronymic()).then(invocationOnMock -> client.getPatronymic());
        Mockito.when(input.getEmail()).then(invocationOnMock -> client.getEmail());
        Mockito.when(input.getPhoneNumber()).then(invocationOnMock -> "787654123&");
        Mockito.when(input.getPassport()).then(invocationOnMock -> client.getPassport());

        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(input));
        assertEquals(CreateClientException.NO_VALID_PHONE, createClientException.getMessage());
    }

    //todo and so on

    @Test
    void testSave() {
    }
}