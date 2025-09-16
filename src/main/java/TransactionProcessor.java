import model.Account;
import model.Transaction;
import model.TransactionStatus;
import repository.AccountRepository;
import repository.TransactionRepository;

public class TransactionProcessor {
    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    public TransactionProcessor(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public synchronized Transaction processTransaction(Transaction transaction) {
        try {
            System.out.println("Processing transaction: " + transaction.getFromAccount()
                    + " -> " + transaction.getToAccount() + " : " + transaction.getAmount());

            transaction.setStatus(TransactionStatus.PROCESSING);
            transactionRepository.saveOrUpdate(transaction);

            switch (transaction.getType()) {
                case DEPOSIT:
                    processDeposit(transaction);
                    break;
                case WITHDRAWAL:
                    processWithdrawal(transaction);
                    break;
                case TRANSFER:
                    processTransfer(transaction);
                    break;
            }

            transaction.setStatus(TransactionStatus.COMPLETE);

            System.out.println("Transaction completed.");

        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);

            System.out.println("Transaction failed.");
        }

        return transactionRepository.saveOrUpdate(transaction);
    }

    private void processDeposit(Transaction transaction) {
        Account account = transaction.getToAccount();
        Double newBalance = account.getBalance() + transaction.getAmount();
        account.setBalance(newBalance);
        accountRepository.saveOrUpdate(account);

        System.out.println("Deposit to account {" + account.getAccountNumber()
                + "}: " + "{" + transaction.getAmount() + "} -> " +
                "{" + newBalance + "}");
    }

    private void processWithdrawal(Transaction transaction) {
        Account account = transaction.getFromAccount();
        Double newBalance = account.getBalance() - transaction.getAmount();

        if (newBalance >= 0) {
            account.setBalance(newBalance);
            accountRepository.saveOrUpdate(account);

            System.out.println("Withdrawal from account {" + account.getAccountNumber()
                    + "}: " + "{" + transaction.getAmount() + "} -> " +
                    "{" + newBalance + "}");
        } else {
            throw new RuntimeException("Withdraw cannot done for account: " + account.getAccountNumber());
        }
    }

    private void processTransfer(Transaction transaction) {
        Account fromAccount = transaction.getFromAccount();
        Account toAccount = transaction.getToAccount();

        Double fromNewBalance = fromAccount.getBalance() - transaction.getAmount();

        if (fromNewBalance >= 0) {

            fromAccount.setBalance(fromNewBalance);

            Double toNewBalance = toAccount.getBalance() + transaction.getAmount();
            toAccount.setBalance(toNewBalance);

            accountRepository.saveOrUpdate(fromAccount);
            accountRepository.saveOrUpdate(toAccount);

            System.out.println("Transfer from {" + fromAccount.getAccountNumber() +
                    "} to {" + toAccount.getAccountNumber() + "}: {" +
                    transaction.getAmount() + "} -> From: {" +
                    fromNewBalance + "}, To: {" + toNewBalance + "}");
        } else {
            throw new RuntimeException("transfer cannot done for account: " + fromAccount.getAccountNumber());
        }
    }
}