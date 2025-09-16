package repository.impl;

import model.SavingAccount;
import repository.SavingAccountRepository;

public class SavingAccountRepositoryImpl extends BaseRepositoryImpl<Long, SavingAccount> implements SavingAccountRepository {
    @Override
    public Class<SavingAccount> getEntityClass() {
        return SavingAccount.class;
    }
}
