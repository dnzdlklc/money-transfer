package com.dnzdlklc.dao;

import com.dnzdlklc.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class AbstractDAOIntegrationTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static List<Customer> clients = new ArrayList<>();

    public static void init() {
        emf = Persistence.createEntityManagerFactory("money-transfer");
        em = emf.createEntityManager();
    }

    public static void tearDown() {
        em.clear();
        em.close();
        emf.close();
    }

    public static EntityManager getEm() {
        return em;
    }

    protected List<Customer> storeClients(List<Customer> toSave) {
        EntityTransaction tx = getEm().getTransaction();
        tx.begin();
        toSave.forEach(getEm()::persist);
        tx.commit();

        clients.addAll(toSave);

        return toSave;
    }

    protected void cleanDB() {
        EntityTransaction tx = getEm().getTransaction();
        tx.begin();
        clients.stream().map(Customer::getId).forEach(
                id -> getEm().remove(getEm().find(Customer.class, id))
        );
        clients.clear();
        tx.commit();
    }

}
