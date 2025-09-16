import model.Account;
import model.Transaction;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportService {

    private static final String REPORT_DIR = "C:\\Users\\abdol\\Desktop\\OnlineBankingSystem\\src\\main\\java\\reports";
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public ReportService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void writeTransactionReport(Long transactionId, String filename) throws IOException {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        Path path = Paths.get(REPORT_DIR + "\\" + filename + ".txt");

        List<String> transactionTransfers = new ArrayList<>();

        transactionTransfers.add(formatTransactionToString(transaction.get()));

        Files.write(path, transactionTransfers);
    }

    public void writeAccountStatusReport(Long accountID, String filename) throws IOException {
        Optional<Account> account = accountRepository.findById(accountID);
        Path path = Paths.get(REPORT_DIR + "\\" + filename + ".txt");

        List<String> accountTransfers = new ArrayList<>();


        accountTransfers.add(formatAccountToString(account.get()));

        Files.write(path, accountTransfers);
    }

    public List<String> readTransactionsFromFile(String filename) throws IOException {
        Path path = Paths.get(REPORT_DIR + filename);
        return Files.readAllLines(path);
    }

    public List<String> readAccountStatusFromFile(String filename) throws IOException {
        Path path = Paths.get(REPORT_DIR + filename);
        return Files.readAllLines(path);
    }

    private String formatTransactionToString(Transaction transaction) {
        return "Transaction= Type: " + transaction.getType() + " - Description: " +
                transaction.getDescription() + " - Amount: " +
                transaction.getAmount() + " - Status: " + transaction.getStatus();
    }

    private String formatAccountToString(Account account) {
        return "Account " + account.getAccountNumber() + "= FirstName: " +
                account.getCustomer().getFirsName() + " - LastName: " +
                account.getCustomer().getLastName() + " - Balance: " +
                account.getBalance();
    }
}