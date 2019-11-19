package com.dnzdlklc;

import com.dnzdlklc.dao.*;
import com.dnzdlklc.rest.Router;
import com.dnzdlklc.rest.RouterImpl;
import com.dnzdlklc.service.ClientAccountService;
import com.dnzdlklc.service.ClientAccountServiceImpl;
import com.dnzdlklc.service.InternalTransferService;
import com.dnzdlklc.service.TransferService;
import com.dnzdlklc.utils.HibernateProxyTypeAdapter;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by denizdalkilic on 2019-11-03 @ 18:16.
 */

@Slf4j
public class AppModule extends AbstractModule {

    private static final String UNDERSCORE = "_";

    @Override
    protected void configure() {
        bind(Gson.class).toInstance(new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().startsWith(UNDERSCORE);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        })
                .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .create());

        bind(AccountDAO.class).to(AccountDAOImpl.class);
        bind(CustomerDAO.class).to(CustomerDAOImpl.class);
        bind(ClientAccountService.class).to(ClientAccountServiceImpl.class);
        bind(TransferService.class).to(InternalTransferService.class);
        bind(Router.class).to(RouterImpl.class);
    }
}