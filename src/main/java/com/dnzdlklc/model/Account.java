package com.dnzdlklc.model;

import com.dnzdlklc.exception.InsufficientFundsException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by denizdalkilic on 2019-11-03 @ 15:55.
 */
@Entity(name = "account")
@Table(name = "account")
@Getter
@Setter
@ToString(exclude = {"_customer", "_lock"})
@EqualsAndHashCode(exclude = {"_customer", "_lock"})
public class Account {

    private static final long serialVersionUID = -558553765744513790L;

    @Transient
    public Lock _lock = new ReentrantLock();

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account-gen")
    @SequenceGenerator(name = "account-gen", sequenceName = "account_sequence", initialValue = 38174103)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", updatable = false, nullable = false)
    private Customer _customer;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(String.format("Insufficient funds to perform withdraw from account: %d", id));
        }
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}