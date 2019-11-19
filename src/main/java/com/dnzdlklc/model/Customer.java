package com.dnzdlklc.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denizdalkilic on 2019-11-03 @ 15:57.
 */
@Entity(name = "customer")
@Table(name = "customer")
@ToString
@EqualsAndHashCode
public class Customer {

    private static final long serialVersionUID = -55855396523490L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="customer-gen")
    @SequenceGenerator(name = "customer-gen", sequenceName = "customer_sequence", initialValue = 689473765)
    @Setter
    @Getter
    private Long id;

    @Column
    @Setter
    @Getter
    private String firstName;

    @Column
    @Setter
    @Getter
    private String lastName;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "_customer", orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        account.set_customer(this);
        accounts.add(account);
    }
}
