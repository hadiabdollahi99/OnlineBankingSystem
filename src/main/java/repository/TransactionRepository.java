package repository;

import model.Transaction;

import java.util.List;

public interface TransactionRepository extends BaseRepository<Long, Transaction> {

    List<Transaction> getAccountTransaction(Long accountId);

}
