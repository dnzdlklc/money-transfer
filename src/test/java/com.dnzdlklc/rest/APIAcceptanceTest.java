package com.dnzdlklc.rest;

import com.dnzdlklc.Application;
import com.dnzdlklc.dao.AbstractDAOIntegrationTest;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import spark.Spark;

@RunWith(Cucumber.class)
public class APIAcceptanceTest {

    @BeforeClass
    public static void start() {
        AbstractDAOIntegrationTest.init();

        String[] args = {};
        Application.main(args);
    }

    @AfterClass
    public static void stop() {
        Spark.stop();
        AbstractDAOIntegrationTest.tearDown();
    }
}
