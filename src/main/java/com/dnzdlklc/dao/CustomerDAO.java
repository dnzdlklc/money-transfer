package com.dnzdlklc.dao;

import com.dnzdlklc.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:04.
 */
public interface CustomerDAO {

    List<Customer> getCustomers();

    Optional<Customer> getCustomer(Long customerId);

}
