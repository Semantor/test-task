package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.CreditDeleteException;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.haulmont.testtask.Setting.NO_VALID_CREDIT;

@Component
@AllArgsConstructor
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreditRemover implements Remover {
    private final CreditRepository creditRepository;

    /**
     * trying to make client {@link Credit#isUnused()}
     *
     * @param credit that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.CreditDeleteException due to failed, fe credit is not persist
     */
    public boolean remove(@Nullable Credit credit) {
        if (credit==null||credit.getCreditId() == null||creditRepository.findById(credit.getCreditId()).isEmpty())
            throw new CreditDeleteException(NO_VALID_CREDIT);

        credit.unused();
        creditRepository.save(credit);
        return true;
    }

    @Override
    public boolean remove(@Nullable Removable removable){
        return remove((Credit) removable);
    }
}
