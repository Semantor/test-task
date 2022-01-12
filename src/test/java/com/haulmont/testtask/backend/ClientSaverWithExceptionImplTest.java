package com.haulmont.testtask.backend;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static com.haulmont.testtask.Setting.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class ClientSaverWithExceptionImplTest {
    @Autowired
    private ClientRepository clientRepository;
    @MockBean
    private ClientFieldsValidator clientFieldsValidator;
    @MockBean
    private Validator validator;
    @MockBean
    private StringUUIDHandler stringUUIDHandler;
    private ClientSaver clientSaver;

    private final Client client = Client.builder()
            .lastName("Ivanov")
            .firstName("Ivan")
            .patronymic("Ivanovich")
            .email("ivan@mail.com")
            .phoneNumber("9876543210")
            .passport("6321789456")
            .build();

    @BeforeEach
    void init() {
        clientSaver = new ClientSaver(clientFieldsValidator, clientRepository, validator, stringUUIDHandler);
    }

    @Test
    void saveNullClient() {
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(null));
        assertEquals(NULLABLE_CLIENT, createClientException.getMessage());
    }

    @Test
    void saveWithNullUUID() {
        ReflectionTestUtils.setField(client, "clientId", null);
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(client));
        assertEquals(NULLABLE_ID, createClientException.getMessage());

    }

    @Test
    void saveWithUserUUIDPresentInDatabase() {
        UUID uuid = client.getClientId();
        Mockito.when(clientRepository.findById(uuid))
                .thenReturn(Optional.of(client));
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(client));
        assertEquals(UUID_IS_ALREADY_USED, createClientException.getMessage());
    }

    @Test
    void saveWithUserNullName() {
        UUID uuid = client.getClientId();
        client.setFirstName(null);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(client));
        assertEquals(EMPTY_NAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserEmptyName() {
        UUID uuid = client.getClientId();
        client.setFirstName("");

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(client));
        assertEquals(EMPTY_NAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserShortName() {
        UUID uuid = client.getClientId();
        client.setFirstName("As");

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(CreateClientException.class, () -> clientSaver.save(client));
        assertEquals(TOO_SHORT_NAME, createClientException.getMessage());

    }

    @Test
    void saveWithUserIncorrectSymbolsName() {
        UUID uuid = client.getClientId();
        client.setFirstName("@ASdw");

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(NAME_INCORRECT_SYMBOLS, createClientException.getMessage());
    }

    @Test
    void saveWithUserNullLastname() {
        UUID uuid = client.getClientId();
        client.setLastName(null);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(EMPTY_LASTNAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserEmptyLastname() {
        UUID uuid = client.getClientId();
        client.setLastName("");

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(EMPTY_LASTNAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserTooShortLastname() {
        UUID uuid = client.getClientId();
        client.setLastName("as");
        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(TOO_SHORT_LASTNAME, createClientException.getMessage());
    }

    @Test
    void saveWithUserIncorrectSymbolsLastname() {
        UUID uuid = client.getClientId();
        client.setLastName("@asdf");
        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(LASTNAME_INCORRECT_SYMBOLS, createClientException.getMessage());
    }


    @Test
    void saveWithUserNullPhone() {
        UUID uuid = client.getClientId();
        client.setPhoneNumber(null);
        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(NO_VALID_PHONE, createClientException.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"","907", "987654123", "787654123"})
    void NoValidPhone(String phone){
        UUID uuid = client.getClientId();
        client.setPhoneNumber(phone);

        Mockito.when(clientFieldsValidator.validatePhone(null)).then(invocationOnMock -> false);
        Mockito.when(clientFieldsValidator.validateEmail(client.getEmail())).then(invocationOnMock -> true);
        Mockito.when(clientFieldsValidator.validatePassport(client.getPassport())).then(invocationOnMock -> true);

        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        CreateClientException createClientException = assertThrows(
                CreateClientException.class,
                () -> clientSaver.save(client));
        assertEquals(NO_VALID_PHONE, createClientException.getMessage());
    }

    //todo and so on
}