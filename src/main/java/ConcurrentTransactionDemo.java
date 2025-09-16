import model.Account;
import model.Transaction;
import model.TransactionStatus;
import model.TransactionType;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentTransactionDemo implements Runnable{
    private TransactionProcessor transactionProcessor;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public ConcurrentTransactionDemo(TransactionProcessor transactionProcessor, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.transactionProcessor = transactionProcessor;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run() {
        System.out.println("=== Concurrent Transaction Processing Demo ===");
        Account testAccount = accountRepository.findByAccountNumber("SAV001")
                .orElseThrow(() -> new RuntimeException("Account not found"));

        System.out.println("Initial balance: " + testAccount.getBalance());

        List<Thread> threads = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Transaction transaction = new Transaction();
            transaction.setFromAccount(testAccount);
            transaction.setAmount(100.00);
            transaction.setType(TransactionType.WITHDRAWAL);
            transaction.setDescription("Concurrent withdrawal " + i);
            transaction.setStatus(TransactionStatus.PENDING);

            transactionRepository.saveOrUpdate(transaction);
        }

        for (Transaction transaction : transactions) {
            Thread thread = new Thread(() -> {
                transactionProcessor.processTransaction(transaction);
            });
            threads.add(thread);
        }


        System.out.println("Starting all threads...");
        for (Thread thread : threads) {
            try {
                thread.start();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Account finalAccount = accountRepository.findByAccountNumber("SAV001")
                .orElseThrow(() -> new RuntimeException("Account not found"));

        System.out.println("Final balance: " + finalAccount.getBalance());
        Double expectedBalance = testAccount.getBalance() - 1000.00;
        System.out.println("Expected balance: " + expectedBalance);

        System.out.println("=== Demo Completed ===");
    }
}