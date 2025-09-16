package service.impl;

import model.SavingAccount;
import repository.SavingAccountRepository;
import service.SavingAccountService;

public class SavingAccountServiceImpl extends BaseServiceImpl<Long, SavingAccount, SavingAccountRepository> implements SavingAccountService {

    protected SavingAccountRepository savingAccountRepository;

    public SavingAccountServiceImpl(SavingAccountRepository savingAccountRepository) {
        super(savingAccountRepository);
        this.savingAccountRepository = savingAccountRepository;
    }
}
