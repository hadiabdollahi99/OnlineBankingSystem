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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction transaction;
    private TransactionProcessor transactionProcessor;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private TransactionRepository transactionRepository;
    private ReportService reportService;


    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("postgres-pu");
        em = emf.createEntityManager();
        transaction = em.getTransaction();
        transactionRepository = new TransActionRepositoryImpl();
        accountRepository = new AccountRepositoryImpl();
        transactionProcessor = new TransactionProcessor(accountRepository, transactionRepository);
        customerRepository = new CustomerRepositoryImpl();
        reportService = new ReportService(transactionRepository, accountRepository);

    }

    @AfterEach
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void testReportTransactionGeneration() throws IOException {
        Customer customer1 = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer1);
        Customer customer2 = new Customer("Sara", "Kim", "sara@Gmail.com", "09198273458");
        customerRepository.saveOrUpdate(customer2);
        Account account1 = new CheckingAccount("CHK001",500.0, customer1);
        accountRepository.saveOrUpdate(account1);
        Account account2= new SavingAccount("SAV001",500.0, customer2);
        accountRepository.saveOrUpdate(account2);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        transaction.setAmount(200.0);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setDescription("Test transfer");
        transactionProcessor.processTransaction(transaction);


        Long transactionId = transaction.getId();

        reportService.writeTransactionReport(transactionId, "transaction_report");

        Path reportPath = Paths.get("C:\\Users\\abdol\\Desktop\\OnlineBankingSystem\\src\\main\\java\\reports\\transaction_report.txt");
        assertTrue(Files.exists(reportPath));
    }


    @Test
    public void testReportAccountGeneration() throws IOException {
        Customer customer1 = new Customer("John", "Doe", "john@Gmail.com", "09122343456");
        customerRepository.saveOrUpdate(customer1);
        Customer customer2 = new Customer("Sara", "Kim", "sara@Gmail.com", "09198273458");
        customerRepository.saveOrUpdate(customer2);
        Account account1 = new CheckingAccount("CHK001",500.0, customer1);
        accountRepository.saveOrUpdate(account1);
        Account account2= new SavingAccount("SAV001",500.0, customer2);
        accountRepository.saveOrUpdate(account2);

        Long accountId = account1.getId();

        reportService.writeAccountStatusReport(accountId, "account_report");

        Path reportPath = Paths.get("C:\\Users\\abdol\\Desktop\\OnlineBankingSystem\\src\\main\\java\\reports\\account_report.txt");
        assertTrue(Files.exists(reportPath));
    }

}