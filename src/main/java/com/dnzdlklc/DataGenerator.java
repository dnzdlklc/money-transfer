package com.dnzdlklc;

import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dnzdlklc.DataGenerator.AccountBuilder.anAccount;
import static com.dnzdlklc.DataGenerator.CustomerBuilder.aCustomer;

/**
 * Created by denizdalkilic on 2019-11-03 @ 18:18.
 */
public class DataGenerator {

    @Inject
    public DataGenerator(EntityManager em) {
        init(em);
    }

    @Transactional
    void init(EntityManager em) {
        em.persist(aCustomer()
                .withFirstName("Deniz")
                .withLastName("Dalkilic")
                .withAccounts(
                        anAccount()
                                .withBalance(BigDecimal.TEN)
                                .build()
                ).build()
        );
        em.persist(aCustomer()
                .withFirstName("Satoshi")
                .withLastName("Nakamoto")
                .withAccounts(
                        anAccount()
                                .withBalance(BigDecimal.valueOf(10000))
                                .build()
                ).build()
        );
        em.persist(aCustomer()
                .withFirstName("Jane")
                .withLastName("Doe")
                .withAccounts(
                        anAccount()
                                .withBalance(BigDecimal.ZERO)
                                .build(),
                        anAccount()
                                .withBalance(BigDecimal.valueOf(100000))
                                .build()
                ).build()
        );
    }

    public static class CustomerBuilder {

        private Long id;
        private String firstName;
        private String lastName;
        private List<Account> accounts = new ArrayList();

        private CustomerBuilder() {
        }

        public static CustomerBuilder aCustomer() {
            return new CustomerBuilder();
        }

        public CustomerBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder withAccounts(Account... accounts) {
            this.accounts.addAll(Arrays.asList(accounts));
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();

            customer.setId(id);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            accounts.forEach(customer::addAccount);

            return customer;
        }
    }

    public static class AccountBuilder {

        private Long id;
        private BigDecimal balance;

        private AccountBuilder() {
        }

        public static AccountBuilder anAccount() {
            return new AccountBuilder();
        }

        public AccountBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AccountBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Account build() {
            Account account = new Account();

            account.setId(id);
            account.setBalance(balance);

            return account;
        }
    }
}
