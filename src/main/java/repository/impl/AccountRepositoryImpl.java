package repository.impl;

import model.Account;
import repository.AccountRepository;
import util.EntityManagerProvider;

import java.util.Optional;

public class AccountRepositoryImpl extends BaseRepositoryImpl<Long, Account> implements AccountRepository {
    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return EntityManagerProvider
                .getEntityManager()
                .createQuery("select a from Account a where a.accountNumber = :accountNumber", Account.class)
                .setParameter("accountNumber", accountNumber)
                .getResultStream()
                .findFirst();
    }
}
