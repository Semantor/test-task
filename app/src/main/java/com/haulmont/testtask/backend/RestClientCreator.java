package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.ClientExchanger;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.dto.ClientDTO;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RestClientCreator {
    private final ClientExchanger clientExchanger;
    private final Validator validator;
    private final ClientRepository clientRepository;

    /**
     * return {@link Result#failure(Exception)} if any exception encountered
     * result will contain {@link Optional#empty()} there are no constraint violation
     */
    public Result<Optional<String>> createNewClientAndReturnResultOfViolationConstraint(ClientDTO clientDTO) {
        try {
            Client client = clientExchanger.exchangeToDO(clientDTO);
            Set<ConstraintViolation<Client>> constraintViolations = validator.validate(client);
            if (constraintViolations.isEmpty())
                return Result.success(Optional.of(ConstraintViolationHandler.handleToString(constraintViolations)));
            clientRepository.save(client);
            return Result.success(Optional.empty());
        } catch (Exception exception) {
            return Result.failure(exception);
        }
    }
}
