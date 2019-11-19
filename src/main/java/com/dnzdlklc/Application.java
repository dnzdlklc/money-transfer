package com.dnzdlklc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Created by denizdalkilic on 2019-11-03 @ 15:51.
 */
public class Application {

    private static final String PERSISTENCE_UNIT_NAME = "money-transfer";

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new com.dnzdlklc.AppModule(), new JpaPersistModule(PERSISTENCE_UNIT_NAME));
        injector.getInstance(AppInitializer.class);
        injector.getInstance(DataGenerator.class);
    }
}
