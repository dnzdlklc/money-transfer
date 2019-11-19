package com.dnzdlklc.dao;

import com.dnzdlklc.model.Customer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:08.
 */
@Slf4j
public class CustomerDAOImpl implements CustomerDAO {

    private final Provider<EntityManager> em;

    @Inject
    public CustomerDAOImpl(Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    public List<Customer> getCustomers() {
        TypedQuery<Customer> query = em.get().createQuery("select c from customer c", Customer.class);

        return query.getResultList();
    }

    @Override
    public Optional<Customer> getCustomer(Long customerId) {
        TypedQuery<Customer> query = em.get()
                .createQuery("select c from customer c where c.id = :customerId", Customer.class)
                .setParameter("customerId", customerId);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            log.warn("No client with id: {}", customerId);
        }

        return Optional.empty();
    }
}

