package com.dnzdlklc.service;

import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;

import java.util.List;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:19.
 */
public interface ClientAccountService {

    List<Customer> getCustomers();

    Customer getCustomer(Long customerId);

    List<Account> getAccounts(Long customerId);

    Account getAccount(Long customerId, Long accountId);

}
