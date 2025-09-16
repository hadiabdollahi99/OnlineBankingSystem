package service.impl;

import model.Account;
import repository.AccountRepository;
import service.AccountService;

import java.util.Optional;

public class AccountServiceImpl extends BaseServiceImpl<Long,Account, AccountRepository> implements AccountService {

    protected AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        super(accountRepository);
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        if (!accountNumber.isEmpty()){
            return accountRepository.findByAccountNumber(accountNumber);
        }
        return Optional.empty();
    }

    @Override
    public Double getAccountBalance(String accountNumber) {
        return findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElse(0.0);
    }
}

