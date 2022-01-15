package com.haulmont.testtask.backend;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.haulmont.testtask.Setting.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class ClientSaverWithExceptionImplTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private javax.validation.Validator validator;
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

    public static Stream<Arguments> valueForWrongFirstName() {
        return Stream.of(
                Arguments.arguments(null, EMPTY_NAME),
                Arguments.arguments("", MUST_BE_MINIMUM_THREE_SYMBOLS),
                Arguments.arguments("As", MUST_BE_MINIMUM_THREE_SYMBOLS),
                Arguments.arguments("@ASdw", MUST_BE_LETTER_ERROR)
        );
    }

    public static Stream<Arguments> valueForWrongLastName() {
        return Stream.of(
                Arguments.arguments(null, EMPTY_LASTNAME),
                Arguments.arguments("", MUST_BE_MINIMUM_THREE_SYMBOLS),
                Arguments.arguments("As", MUST_BE_MINIMUM_THREE_SYMBOLS),
                Arguments.arguments("@ASdw", MUST_BE_LETTER_ERROR)
        );
    }

    public static Stream<Arguments> valueForWrongPhone() {
        return Stream.of(
                Arguments.arguments(null, EMPTY_PHONE_NUMBER),
                Arguments.arguments("asdf",PHONE_PATTERN_ERROR_MSG),
                Arguments.arguments("", PHONE_LENGTH_ERROR_MSG),
                Arguments.arguments("987654123", PHONE_PATTERN_ERROR_MSG),
                Arguments.arguments("787654123", PHONE_LENGTH_ERROR_MSG)
        );
    }

    @BeforeEach
    void init() {
        clientSaver = new ClientSaver(clientRepository, stringUUIDHandler, validator);
    }


    @Test
    void saveWithNullUUID() {
        ReflectionTestUtils.setField(client, "clientId", null);
        Result<Boolean> isSaved = clientSaver.save(client);
        assertTrue(isSaved.isFailure());
        assertTrue(isSaved.exceptionOrNull().getMessage().contains(NULLABLE_ID));
    }

    @Test
    void saveWithUserUUIDPresentInDatabase() {
        UUID uuid = client.getClientId();
        Mockito.when(clientRepository.findById(uuid))
                .thenReturn(Optional.of(client));
        Result<Boolean> isSaved = clientSaver.save(client);
        assertTrue(isSaved.isFailure());
        assertEquals(UUID_IS_ALREADY_USED, isSaved.exceptionOrNull().getMessage());
    }

    @ParameterizedTest
    @MethodSource("valueForWrongFirstName")
    void saveWithWrongFirstName(String firstName, String errorMsg) {
        UUID uuid = client.getClientId();
        client.setFirstName(firstName);
        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());
        Result<Boolean> isSaved = clientSaver.save(client);
        assertTrue(isSaved.isFailure());
        assertTrue(isSaved.exceptionOrNull().getMessage().contains(errorMsg));
    }


    @ParameterizedTest
    @MethodSource("valueForWrongLastName")
    void saveWithWrongLastName(String lastName, String errorMsg) {
        UUID uuid = client.getClientId();
        client.setLastName(lastName);
        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());

        Result<Boolean> isSaved = clientSaver.save(client);
        assertTrue(isSaved.isFailure());
        assertTrue(isSaved.exceptionOrNull().getMessage().contains(errorMsg));
    }

    @ParameterizedTest
    @MethodSource("valueForWrongPhone")
    void saveWithUserNullPhone(String phone, String errorMsg) {
        UUID uuid = client.getClientId();
        client.setPhoneNumber(phone);
        Mockito.when(clientRepository.findById(uuid)).then(invocationOnMock -> Optional.empty());

        Result<Boolean> isSaved = clientSaver.save(client);
        assertTrue(isSaved.isFailure());
        assertTrue(isSaved.exceptionOrNull().getMessage().contains(errorMsg));
    }

    //todo and so on
}