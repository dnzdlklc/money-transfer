package com.dnzdlklc.service;

import com.dnzdlklc.dao.AccountDAO;
import com.dnzdlklc.dao.CustomerDAO;
import com.dnzdlklc.exception.AccountNotFoundException;
import com.dnzdlklc.exception.CustomerNotFoundException;
import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:19.
 */
public class ClientAccountServiceImpl implements ClientAccountService {
    private final AccountDAO accountDao;
    private final CustomerDAO customerDao;

    @Inject
    private ClientAccountServiceImpl(AccountDAO accountDao, CustomerDAO customerDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerDao.getCustomers();
    }

    @Override
    public Customer getCustomer(Long customerId) {
        return customerDao.getCustomer(customerId).orElseThrow(() ->
                new CustomerNotFoundException(String.format("Customer with id: %s cannot be found", customerId)));

    }

    @Override
    public List<Account> getAccounts(Long customerId) {
        return accountDao.getAccounts(customerId);
    }

    @Override
    public Account getAccount(Long customerId, Long accountId) {
        return accountDao.getAccount(customerId, accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id: %s cannot be found", accountId)));
    }
}