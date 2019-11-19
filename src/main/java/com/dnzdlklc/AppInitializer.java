package com.dnzdlklc;

import com.dnzdlklc.rest.Router;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

/**
 * Created by denizdalkilic on 2019-11-03 @ 18:24.
 */
public class AppInitializer {
    @Inject
    public AppInitializer(PersistService persistService, Router router) {
        persistService.start();
        router.start();
    }
}