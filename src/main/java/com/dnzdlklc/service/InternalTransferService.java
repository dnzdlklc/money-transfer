package com.dnzdlklc.service;

import com.dnzdlklc.dao.AccountDAO;
import com.dnzdlklc.dto.TransferDTO;
import com.dnzdlklc.exception.AccountNotFoundException;
import com.dnzdlklc.exception.InvalidAmountValueException;
import com.dnzdlklc.model.Account;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:24.
 */
public class InternalTransferService implements TransferService {
    private static final Random rnd = new Random();
    private static final long FIXED_DELAY = 1;
    private static final long RANDOM_DELAY = 2;
    private static final long TIMEOUT = TimeUnit.SECONDS.toNanos(2);

    private final AccountDAO accountDao;

    @Inject
    public InternalTransferService(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public boolean transfer(TransferDTO transfer) {
        validateAmount(transfer.getAmount());

        long stopTime = System.nanoTime() + TIMEOUT;

        while (true) {
            Account fromAccount, toAccount;

            synchronized (this) {
                fromAccount = accountDao.getAccount(transfer.getFromAccount())
                        .orElseThrow(() -> new AccountNotFoundException(
                                format("Account with id %d (fromAccount) cannot be found", transfer.getFromAccount())));

                toAccount = accountDao.getAccount(transfer.getToAccount())
                        .orElseThrow(() -> new AccountNotFoundException(
                                format("Account with id %d (toAccount) cannot be found", transfer.getToAccount())));

            }

            if (fromAccount._lock.tryLock()) {
                try {
                    if (toAccount._lock.tryLock()) {
                        try {
                            fromAccount.withdraw(transfer.getAmount());
                            toAccount.deposit(transfer.getAmount());

                            accountDao.save(fromAccount, toAccount);

                            return true;
                        } finally {
                            toAccount._lock.unlock();
                        }
                    }
                } finally {
                    fromAccount._lock.unlock();
                }
            }
            if (System.nanoTime() > stopTime) {
                return false;
            }
            try {
                TimeUnit.NANOSECONDS.sleep(FIXED_DELAY + rnd.nextLong() % RANDOM_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.floatValue() <= 0) {
            throw new InvalidAmountValueException("Amount value has to be higher than 0");
        }
    }
}