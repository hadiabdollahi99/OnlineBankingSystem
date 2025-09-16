import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import repository.impl.AccountRepositoryImpl;
import repository.impl.CustomerRepositoryImpl;
import repository.impl.TransActionRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankingSystemTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction transaction;
    private TransactionProcessor transactionProcessor;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private TransactionRepository transactionRepository;


    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("postgres-pu");
        em = emf.createEntityManager();
        transaction = em.getTransaction();
        transactionRepository = new TransActionRepositoryImpl();
        accountRepository = new AccountRepositoryImpl();
        transactionProcessor = new TransactionProcessor(accountRepository, transactionRepository);
        customerRepository = new CustomerRepositoryImpl();

    }

    @AfterEach
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void testDepositTransaction() {
        Customer customer = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer);
        Account account = new CheckingAccount("CHK001",100.0, customer);
        accountRepository.saveOrUpdate(account);

        Double initialBalance = account.getBalance();
        Transaction transaction = new Transaction();
        transaction.setToAccount(account);
        transaction.setAmount(500.00);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Test deposit");
        transactionProcessor.processTransaction(transaction);

        Account updatedAccount = accountRepository.findByAccountNumber("CHK001").orElseThrow();
        assertEquals(initialBalance + 500.00 , updatedAccount.getBalance());
    }

    @Test
    public void testWithdrawTransaction() {
        Customer customer = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer);
        Account account = new CheckingAccount("CHK001",700.0, customer);
        accountRepository.saveOrUpdate(account);

        Double initialBalance = account.getBalance();
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account);
        transaction.setAmount(500.00);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setDescription("Test withdraw");
        transactionProcessor.processTransaction(transaction);

        Account updatedAccount = accountRepository.findByAccountNumber("CHK001").orElseThrow();
        assertEquals(initialBalance - 500.00 , updatedAccount.getBalance());
    }

    @Test
    public void testTransferTransaction() {
        Customer customer1 = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer1);
        Customer customer2 = new Customer("Sara", "Kim", "sara@Gmail.com", "09198273458");
        customerRepository.saveOrUpdate(customer2);
        Account account1 = new CheckingAccount("CHK001",500.0, customer1);
        accountRepository.saveOrUpdate(account1);
        Account account2= new SavingAccount("SAV001",500.0, customer2);
        accountRepository.saveOrUpdate(account2);

        Double initialBalance1 = account1.getBalance();
        Double initialBalance2 = account2.getBalance();
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        transaction.setAmount(200.0);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setDescription("Test transfer");
        transactionProcessor.processTransaction(transaction);

        Account updatedAccount1 = accountRepository.findByAccountNumber("CHK001").orElseThrow();
        Account updatedAccount2 = accountRepository.findByAccountNumber("SAV001").orElseThrow();
        assertEquals(initialBalance1 - 200.0 , updatedAccount1.getBalance());
        assertEquals(initialBalance2 + 200.0 , updatedAccount2.getBalance());
    }

    @Test
    public void testConcurrentTransactionsWithdraw() throws InterruptedException {
        Customer customer = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer);
        Account account = new CheckingAccount("CHK001",500.0, customer);
        accountRepository.saveOrUpdate(account);

        Double initialBalance = account.getBalance();
        int numberOfTransactions = 5;

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction transaction = new Transaction();
            transaction.setFromAccount(account);
            transaction.setAmount(50.00);
            transaction.setType(TransactionType.WITHDRAWAL);
            transaction.setDescription("Test withdrawal " + i);
            transaction.setStatus(TransactionStatus.PENDING);
            transactions.add(transactionRepository.saveOrUpdate(transaction));
        }


        List<Thread> threads = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Thread thread = new Thread(() -> {
                transactionProcessor.processTransaction(transaction);
            });
            threads.add(thread);
        }

        for (Thread thread : threads){
            thread.start();
        }


        for (Thread thread : threads) {
            thread.join();
        }

        Account finalAccount = accountRepository.findByAccountNumber("CHK001")
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Double expectedBalance = initialBalance - (numberOfTransactions * 50.0);

        assertEquals(expectedBalance, finalAccount.getBalance());
    }

    @Test
    public void testConcurrentTransactionsTransfer() throws InterruptedException {
        Customer customer1 = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer1);
        Customer customer2 = new Customer("Sara", "Kim", "sara@Gmail.com", "09198273458");
        customerRepository.saveOrUpdate(customer2);
        Account account1 = new CheckingAccount("CHK001",500.0, customer1);
        accountRepository.saveOrUpdate(account1);
        Account account2= new SavingAccount("SAV001",500.0, customer2);
        accountRepository.saveOrUpdate(account2);


        Double initialBalance1 = account1.getBalance();
        Double initialBalance2 = account2.getBalance();

        int numberOfTransactions = 5;

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction transaction = new Transaction();
            transaction.setFromAccount(account1);
            transaction.setToAccount(account2);
            transaction.setAmount(50.00);
            transaction.setType(TransactionType.TRANSFER);
            transaction.setDescription("Test Transfer " + i);
            transaction.setStatus(TransactionStatus.PENDING);
            transactions.add(transactionRepository.saveOrUpdate(transaction));
        }

        List<Thread> threads = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Thread thread = new Thread(() -> {
                transactionProcessor.processTransaction(transaction);
            });
            threads.add(thread);
        }

        for (Thread thread : threads){
            thread.start();
        }


        for (Thread thread : threads) {
            thread.join();
        }


        Account finalAccount1 = accountRepository.findByAccountNumber("CHK001").orElseThrow();
        Account finalAccount2 = accountRepository.findByAccountNumber("SAV001").orElseThrow();

        Double expectedBalance1 = initialBalance1 - (numberOfTransactions * 50.0);
        Double expectedBalance2 = initialBalance2 + (numberOfTransactions * 50.0);

        assertEquals(expectedBalance1, finalAccount1.getBalance());
        assertEquals(expectedBalance2, finalAccount2.getBalance());
    }



    @Test
    public void testAccountCreationAndRetrieval() {
        Customer customer = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer);
        Account account = new CheckingAccount("CHK001",500.0, customer);
        accountRepository.saveOrUpdate(account);



        Account finalAccount1 = accountRepository.findByAccountNumber("CHK001").orElseThrow();
        assertEquals(500.00, finalAccount1.getBalance());
    }

    @Test
    public void testWithdrawCannotDone() {
        Customer customer = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer);
        Account account = new CheckingAccount("CHK001",500.0, customer);
        accountRepository.saveOrUpdate(account);

        Double largeAmount = account.getBalance() * 2.0;

        Transaction transaction = new Transaction();
        transaction.setFromAccount(account);
        transaction.setAmount(largeAmount);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setDescription("Large withdrawal");
        transactionProcessor.processTransaction(transaction);

        assertEquals("FAILED", transaction.getStatus().name());


    }

}
