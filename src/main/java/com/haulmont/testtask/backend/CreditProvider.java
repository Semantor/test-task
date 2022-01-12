package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class CreditProvider {
    private final CreditRepository creditRepository;
    private final StringUUIDHandler stringUUIDHandler;

    public List<Credit> getAllCredit() {
        return creditRepository.findByIsUnused(false);
    }

    /**
     * validate UUID
     */
    public Optional<Credit> getCredit(@Nullable String uuidString) {
        UUID uuid = stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (uuid == null) return Optional.empty();
        Optional<Credit> byId = creditRepository.findById(uuid);
        if (byId.isEmpty() || byId.get().isUnused()) return Optional.empty();
        return byId;
    }
}
