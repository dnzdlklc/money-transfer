package com.dnzdlklc.service;

import com.dnzdlklc.dao.AccountDAO;
import com.dnzdlklc.dao.CustomerDAO;
import com.dnzdlklc.exception.AccountNotFoundException;
import com.dnzdlklc.exception.CustomerNotFoundException;
import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;
import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dnzdlklc.DataGenerator.AccountBuilder.anAccount;
import static com.dnzdlklc.DataGenerator.CustomerBuilder.aCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAccountServiceImplTest {

    @Mock
    private AccountDAO accountDao;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private ClientAccountServiceImpl underTest;

    @Test
    public void returnsAllClients() {
        // given
        Customer c1 = aCustomer()
                .withId(11111L)
                .withFirstName("Abc")
                .withLastName("Qaz")
                .build();
        Customer c2 = aCustomer()
                .withId(22222L)
                .withFirstName("Zxc")
                .withLastName("Qwe")
                .build();
        when(customerDAO.getCustomers()).thenReturn(Lists.newArrayList(c1, c2));

        // when
        List<Customer> actual = underTest.getCustomers();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    @Test
    public void returnsEmptyListWhenNoClients() {
        // given
        when(customerDAO.getCustomers()).thenReturn(Lists.newArrayList());

        // when
        List<Customer> actual = underTest.getCustomers();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsSingleClient() {
        // given
        Customer c1 = aCustomer()
                .withId(33333L)
                .withFirstName("Abc")
                .withLastName("Qaz")
                .build();
        when(customerDAO.getCustomer(c1.getId())).thenReturn(Optional.of(c1));

        // when
        Customer actual = underTest.getCustomer(c1.getId());

        // then
        assertThat(actual).isEqualTo(c1);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void throwsExceptionWhenClientNotFound() {
        // given
        when(customerDAO.getCustomer(any(Long.class))).thenReturn(Optional.empty());

        // when
        underTest.getCustomer(5656565L);
    }

    @Test
    public void returnsEmptyAccountListWhenNoAccountsForGivenClient() {
        // given
        Long customerId = 101010L;
        when(accountDao.getAccounts(customerId)).thenReturn(Collections.emptyList());

        // when
        List<Account> actual = underTest.getAccounts(customerId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsAllAccountsForGivenClient() {
        // given
        Long clientId = 3333222L;
        Account a1 = anAccount()
                .withId(1L)
                .withBalance(BigDecimal.ZERO)
                .build();
        Account a2 = anAccount()
                .withId(2L)
                .withBalance(BigDecimal.TEN)
                .build();
        List<Account> accounts = ImmutableList.of(a1, a2);
        when(accountDao.getAccounts(clientId)).thenReturn(accounts);

        // when
        List<Account> actual = underTest.getAccounts(clientId);

        // then
        assertThat(actual).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    public void returnsSingleAccountForGivenClient() {
        // given
        Long clientId = 44444L;
        long accountId = 1L;
        Account a = anAccount()
                .withId(accountId)
                .withBalance(BigDecimal.TEN)
                .build();
        when(accountDao.getAccount(clientId, 1L)).thenReturn(Optional.of(a));

        // when
        Account actual = underTest.getAccount(clientId, accountId);

        // then
        assertThat(actual).isEqualTo(a);
    }

    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenAccountNotFoundForGivenUser() {
        // given
        long nonExistingAccountId = 1L;
        Long customerId = 55555L;
        when(accountDao.getAccount(customerId, nonExistingAccountId)).thenReturn(Optional.empty());

        // when
        underTest.getAccount(customerId, nonExistingAccountId);
    }
}
