package service.impl;

import model.Transaction;
import repository.TransactionRepository;
import service.TransactionService;

import java.util.List;

public class TransactionServiceImpl extends BaseServiceImpl<Long, Transaction, TransactionRepository> implements TransactionService {

    protected TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.transactionRepository= transactionRepository;
    }

    @Override
    public List<Transaction> getAccountTransaction(Long accountId) {
        return transactionRepository.getAccountTransaction(accountId);
    }
}
