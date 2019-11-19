package com.dnzdlklc.dao;

import com.dnzdlklc.model.Customer;
import org.assertj.core.util.Lists;
import org.junit.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dnzdlklc.DataGenerator.CustomerBuilder.aCustomer;
import static org.assertj.core.api.Assertions.assertThat;

public class ClientDAOImplIntegrationTest extends AbstractDAOIntegrationTest {

    private CustomerDAO underTest;

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
        underTest = new CustomerDAOImpl(AbstractDAOIntegrationTest::getEm);
    }

    @After
    public void clearDB() {
        cleanDB();
    }

    @Test
    public void returnsEmptyListWhenNoClients() {
        // when
        List<Customer> actual = underTest.getCustomers();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsAllClients() {
        // given
        Customer c1 = aCustomer()
                .withFirstName("Abc")
                .withLastName("Qwe")
                .build();
        Customer c2 = aCustomer()
                .withFirstName("Yui")
                .withLastName("Bnm")
                .build();
        storeClients(Lists.newArrayList(c1, c2));

        // when
        List<Customer> actual = underTest.getCustomers();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    @Test
    public void returnsOptionalWithSingleClient() {
        // given
        Customer c1 = aCustomer()
                .withFirstName("Abc")
                .withLastName("Qwe")
                .build();
        storeClients(Lists.newArrayList(c1));

        // when
        Optional<Customer> actual = underTest.getCustomer(c1.getId());

        // then
        assertThat(actual.get()).isEqualTo(c1);
    }

    @Test
    public void returnsEmptyOptionalWhenClientNotFound() {
        // given
        Long nonExistingClientId = 55555555L;

        // when
        Optional<Customer> actual = underTest.getCustomer(nonExistingClientId);

        // then
        assertThat(actual.isPresent()).isFalse();
    }

}