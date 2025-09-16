package repository.impl;

import model.Transaction;
import repository.TransactionRepository;
import util.EntityManagerProvider;

import java.util.List;
import java.util.stream.Collectors;

public class TransActionRepositoryImpl extends BaseRepositoryImpl<Long, Transaction> implements TransactionRepository {
    @Override
    public Class<Transaction> getEntityClass() {
        return Transaction.class;
    }

    @Override
    public List<Transaction> getAccountTransaction(Long accountId) {
        String jpql = "select t from Transaction t" +
                " where t.fromAccount.id = :accountId" +
                " or t.toAccount.id = :accountId" +
                " order by t.createdAt desc";

        return EntityManagerProvider
                .getEntityManager()
                .createQuery("select t from Transaction t" +
                        " where t.fromAccount.id = :accountId" +
                        " or t.toAccount.id = :accountId" +
                        " order by t.createdAt desc", Transaction.class)
                .setParameter("accountId", accountId)
                .getResultStream()
                .collect(Collectors.toList());
    }
}
