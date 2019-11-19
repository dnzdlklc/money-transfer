package com.dnzdlklc.dao;

import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;
import org.assertj.core.util.Lists;
import org.junit.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.dnzdlklc.DataGenerator.AccountBuilder.anAccount;
import static com.dnzdlklc.DataGenerator.CustomerBuilder.aCustomer;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountDAOImplIntegrationTest extends AbstractDAOIntegrationTest {

    private AccountDAO underTest;

    @BeforeClass
    public static void init() {
        AbstractDAOIntegrationTest.init();
    }

    @AfterClass
    public static void tearDown() {
        AbstractDAOIntegrationTest.tearDown();
    }

    @Before
    public void setUp() {
        underTest = new AccountDAOImpl(AbstractDAOIntegrationTest::getEm);
    }

    @After
    public void clearDB() {
        cleanDB();
    }

    @Test
    public void returnsAllAccountsForGivenClient() {
        // given
        Account a1 = anAccount().withBalance(BigDecimal.ZERO).build();
        Account a2 = anAccount().withBalance(BigDecimal.TEN).build();
        Customer c = aCustomer()
                .withFirstName("Abc")
                .withLastName("Zxc")
                .withAccounts(a1, a2)
                .build();
        storeClients(Lists.newArrayList(c));

        // when
        List<Account> actual = underTest.getAccounts(c.getId());

        // then
        assertThat(actual).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    public void returnsEmptyListWhenNoAccountsForGivenClient() {
        // when
        List<Account> accounts = underTest.getAccounts(32434L);

        // then
        assertThat(accounts).isEmpty();
    }

    @Test
    public void returnsOptionalWithSingleAccountForGivenClient() {
        // given
        Account a = anAccount()
                .withBalance(BigDecimal.ZERO)
                .build();
        Customer c = aCustomer()
                .withFirstName("Abc")
                .withLastName("Zxc")
                .withAccounts(a)
                .build();
        storeClients(Lists.newArrayList(c));

        // when
        Optional<Account> account = underTest.getAccount(c.getId(), a.getId());

        assertThat(account.get()).isEqualTo(a);
    }

    @Test
    public void returnsEmptyOptionalWhenAccountNotFound() {
        // given
        long nonExistingAccountId = 0L;
        Long customerId = 65790890845L;

        // when
        Optional<Account> actual = underTest.getAccount(customerId, nonExistingAccountId);

        // then
        assertThat(actual.isPresent()).isFalse();
    }
}