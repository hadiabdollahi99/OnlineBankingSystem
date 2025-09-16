package service;

import model.Account;

import java.util.Optional;

public interface AccountService extends BaseService<Long, Account> {
    Optional<Account> findByAccountNumber(String accountNumber);

    Double getAccountBalance(String accountNumber);
}
