package com.dnzdlklc.dao;

import com.dnzdlklc.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:03.
 */
public interface AccountDAO {

    List<Account> getAccounts(Long accountId);

    Optional<Account> getAccount(Long customerId, Long accountId);

    Optional<Account> getAccount(Long accountId);

    void save(Account... accounts);

}