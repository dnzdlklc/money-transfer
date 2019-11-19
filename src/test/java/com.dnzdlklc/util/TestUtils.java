package com.dnzdlklc.util;

import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;

import java.math.BigDecimal;
import java.util.List;

public class TestUtils {

    public static Customer getCustomerByFirstAndLastName(List<Customer> clients, String firstName, String lastName) {
        return clients.stream()
                .filter(client -> firstName.equals(client.getFirstName()) && lastName.equals(client.getLastName()))
                .findFirst()
                .orElse(null);
    }

    public static Long extractCustomerId(Customer customer) {
        return customer.getId();
    }

    public static Account extractAccount(Customer customer) {
        return customer.getAccounts().stream().findFirst().orElse(null);
    }

    public static BigDecimal toBigDecimal(String balance) {
        return new BigDecimal(balance);
    }
}
