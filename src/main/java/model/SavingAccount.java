package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Saving account")
public class SavingAccount extends Account{
    private static final double ratePerMonth = 4;

    public SavingAccount() {
    }

    public SavingAccount(String accountNumber, Double balance, Customer customer) {
        super(accountNumber, balance*ratePerMonth, customer);
    }

}
