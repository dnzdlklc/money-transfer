package com.dnzdlklc.service;

import com.dnzdlklc.dao.AbstractDAOIntegrationTest;
import com.dnzdlklc.dao.AccountDAOImpl;
import com.dnzdlklc.dto.TransferDTO;
import com.dnzdlklc.model.Account;
import com.dnzdlklc.model.Customer;
import com.google.common.collect.Lists;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static com.dnzdlklc.DataGenerator.AccountBuilder.anAccount;
import static com.dnzdlklc.DataGenerator.CustomerBuilder.aCustomer;
import static org.assertj.core.api.Assertions.assertThat;

public class InternalTransferServiceConcurrencyTest extends AbstractDAOIntegrationTest {

    private static final int NUM_OF_THREADS = 1000;

    private AccountDAOImpl accountDao;
    private TransferService underTest;

    @BeforeClass
    public static void init() {
        AbstractDAOIntegrationTest.init();
    }

    @Before
    public void setUp() {
        accountDao = new AccountDAOImpl(AbstractDAOIntegrationTest::getEm);
        underTest = new InternalTransferService(accountDao);
    }

    @After
    public void clearDB() {
        cleanDB();
    }

    @AfterClass
    public static void tearDown() {
        AbstractDAOIntegrationTest.tearDown();
    }

    @Test
    public void ensuresThreadSafetyForConcurrentTransfers() throws InterruptedException {
        Account fromAccount = anAccount()
                .withBalance(BigDecimal.valueOf(1000000.55))
                .build();
        Account toAccount = anAccount()
                .withBalance(BigDecimal.valueOf(500000.55))
                .build();
        Customer client = aCustomer()
                .withFirstName("John")
                .withLastName("Doe")
                .withAccounts(fromAccount, toAccount)
                .build();
        storeClients(Lists.newArrayList(client));

        Collection<Callable<Boolean>> tasks = new ArrayList<>(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            tasks.add(() -> underTest.transfer(createTransferRequest(fromAccount, toAccount, 1.00F)));
            tasks.add(() -> underTest.transfer(createTransferRequest(toAccount, fromAccount, 2.00F)));
        }

        Executors.newFixedThreadPool(4).invokeAll(tasks);

        assertThat(fromAccount.getBalance()).isEqualTo(BigDecimal.valueOf(1001000.55));
        assertThat(toAccount.getBalance()).isEqualTo(BigDecimal.valueOf(499000.55));
        assertThat(accountDao.getAccount(fromAccount.getId()).get().getBalance()).isEqualTo(BigDecimal.valueOf(1001000.55));
        assertThat(accountDao.getAccount(toAccount.getId()).get().getBalance()).isEqualTo(BigDecimal.valueOf(499000.55));
    }

    private TransferDTO createTransferRequest(Account fromAccount, Account toAccount, float amount) {
        TransferDTO transferRequest = new TransferDTO();
        transferRequest.setFromAccount(fromAccount.getId());
        transferRequest.setToAccount(toAccount.getId());
        transferRequest.setAmount(BigDecimal.valueOf(amount));
        return transferRequest;
    }
}