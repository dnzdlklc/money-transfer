package com.dnzdlklc.service;

import com.dnzdlklc.dao.AccountDAO;
import com.dnzdlklc.dto.TransferDTO;
import com.dnzdlklc.exception.AccountNotFoundException;
import com.dnzdlklc.exception.InsufficientFundsException;
import com.dnzdlklc.exception.InvalidAmountValueException;
import com.dnzdlklc.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static com.dnzdlklc.DataGenerator.AccountBuilder.anAccount;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternalTransferServiceTest {

    @Mock
    private AccountDAO accountDao;

    @InjectMocks
    private InternalTransferService underTest;

    @Test
    public void transfersMoneyBetweenTwoAccounts() {
        // given
        Account fromAccount = anAccount()
                .withId(0L)
                .withBalance(new BigDecimal("900.00"))
                .build();
        Account toAccount = anAccount()
                .withId(1L)
                .withBalance(new BigDecimal("600.00"))
                .build();

        TransferDTO request = new TransferDTO();
        request.setFromAccount(0L);
        request.setToAccount(1L);
        request.setAmount(BigDecimal.valueOf(200));
        when(accountDao.getAccount(0L)).thenReturn(Optional.of(fromAccount));
        when(accountDao.getAccount(1L)).thenReturn(Optional.of(toAccount));

        // when
        underTest.transfer(request);

        // then
        assertThat(fromAccount.getBalance()).isEqualTo(new BigDecimal("700.00"));
        assertThat(toAccount.getBalance()).isEqualTo(new BigDecimal("800.00"));
    }

    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenFromAccountNotFound() {
        // given
        long nonExistingAccountId = 0L;
        TransferDTO request = new TransferDTO();
        request.setFromAccount(nonExistingAccountId);
        request.setAmount(BigDecimal.valueOf(1));

        // when
        underTest.transfer(request);
    }


    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenToAccountNotFound() {
        // given
        long nonExistingAccountId = 0L;
        TransferDTO request = new TransferDTO();
        request.setToAccount(nonExistingAccountId);
        request.setAmount(BigDecimal.valueOf(1));

        // when
        underTest.transfer(request);
    }

    @Test(expected = InvalidAmountValueException.class)
    public void throwsExceptionWhenAmountNotHigherThanZero() {
        // given
        TransferDTO request = new TransferDTO();
        request.setAmount(BigDecimal.ZERO);

        // when
        underTest.transfer(request);
    }

    @Test(expected = InsufficientFundsException.class)
    public void throwsExceptionWhenInsufficientFunds() {
        // given
        TransferDTO request = new TransferDTO();
        request.setToAccount(0L);
        request.setFromAccount(1L);
        request.setAmount(BigDecimal.valueOf(1000));
        Account account = anAccount()
                .withBalance(new BigDecimal("900.00"))
                .build();
        when(accountDao.getAccount(anyLong())).thenReturn(Optional.of(account));

        // when
        underTest.transfer(request);
    }

}