package repository.impl;

import model.Customer;
import repository.CustomerRepository;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Long, Customer> implements CustomerRepository {
    @Override
    public Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
