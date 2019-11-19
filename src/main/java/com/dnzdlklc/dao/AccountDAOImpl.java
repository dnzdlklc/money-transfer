package com.dnzdlklc.dao;

import com.dnzdlklc.model.Account;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:05.
 */
@Slf4j
public class AccountDAOImpl implements AccountDAO {

    private final Provider<EntityManager> em;

    @Inject
    public AccountDAOImpl(Provider<EntityManager> em) {
        this.em = em;
    }


    @Override
    public List<Account> getAccounts(Long accountId) {
        List<Account> accounts = em.get().createQuery("select a from account a where a._customer.id = :customerId", Account.class)
                .setParameter("customerId", accountId)
                .getResultList();

        return accounts;
    }

    @Override
    public Optional<Account> getAccount(Long customerId, Long accountId) {
        TypedQuery<Account> query = em.get().createQuery(
                "select a from account a where a._customer.id = :customerId and a.id = :accountId", Account.class)
                .setParameter("customerId", customerId)
                .setParameter("accountId", accountId);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            log.warn("No account with id: {}", accountId);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> getAccount(Long accountId) {
        TypedQuery<Account> query = em.get().createQuery(
                "select a from account a where a.id = :accountId", Account.class)
                .setParameter("accountId", accountId);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            log.warn("No account with id: {}", accountId);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void save(Account... accounts) {
        Arrays.asList(accounts).forEach(em.get()::persist);
    }
}
