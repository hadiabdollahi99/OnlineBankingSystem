package service;

import model.Transaction;

import java.util.List;

public interface TransactionService extends BaseService<Long, Transaction>{

    List<Transaction> getAccountTransaction(Long accountId);

}
