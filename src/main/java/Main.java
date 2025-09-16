import model.*;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import repository.impl.AccountRepositoryImpl;
import repository.impl.CustomerRepositoryImpl;
import repository.impl.TransActionRepositoryImpl;
import service.AccountService;
import service.CustomerService;
import service.TransactionService;
import service.impl.AccountServiceImpl;
import service.impl.CustomerServiceImpl;
import service.impl.TransactionServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        TransactionRepository transactionRepository = new TransActionRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository);
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);


        TransactionProcessor tp = new TransactionProcessor(accountRepository,transactionRepository);
        ConcurrentTransactionDemo ctd = new ConcurrentTransactionDemo(tp, accountRepository, transactionRepository);
        ReportService reportService = new ReportService(transactionRepository,accountRepository);
    }
}
