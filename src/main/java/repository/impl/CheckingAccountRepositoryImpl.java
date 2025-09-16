package repository.impl;

import model.CheckingAccount;
import repository.CheckingAccountRepository;

public class CheckingAccountRepositoryImpl extends BaseRepositoryImpl<Long, CheckingAccount> implements CheckingAccountRepository {
    @Override
    public Class<CheckingAccount> getEntityClass() {
        return CheckingAccount.class;
    }
}
