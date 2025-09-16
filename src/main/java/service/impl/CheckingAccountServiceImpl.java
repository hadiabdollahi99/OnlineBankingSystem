package service.impl;

import model.CheckingAccount;
import repository.CheckingAccountRepository;
import service.CheckingAccountService;

public class CheckingAccountServiceImpl extends BaseServiceImpl<Long, CheckingAccount, CheckingAccountRepository> implements CheckingAccountService {
    protected CheckingAccountRepository checkingAccountRepository;

    public CheckingAccountServiceImpl(CheckingAccountRepository checkingAccountRepository) {
        super(checkingAccountRepository);
        this.checkingAccountRepository = checkingAccountRepository;
    }
}
