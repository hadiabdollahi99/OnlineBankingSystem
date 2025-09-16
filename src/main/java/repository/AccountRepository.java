package repository;

import model.Account;

import java.util.Optional;

public interface AccountRepository extends BaseRepository<Long, Account>{

    Optional<Account> findByAccountNumber(String accountNumber);
}
