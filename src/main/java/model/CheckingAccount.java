package model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Checking account")
public class CheckingAccount extends Account{
    private static final double ratePerMonth = 1;

    public CheckingAccount() {
    }

    public CheckingAccount(String accountNumber, Double balance, Customer customer) {
        super(accountNumber, balance*ratePerMonth, customer);
    }

}
