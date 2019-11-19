package com.dnzdlklc.rest;

import com.dnzdlklc.dto.TransferDTO;
import com.dnzdlklc.exception.AccountNotFoundException;
import com.dnzdlklc.exception.CustomerNotFoundException;
import com.dnzdlklc.exception.InsufficientFundsException;
import com.dnzdlklc.exception.InvalidAmountValueException;
import com.dnzdlklc.service.ClientAccountService;
import com.dnzdlklc.service.TransferService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import static spark.Spark.*;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:18.
 */
@Slf4j
public class RouterImpl implements Router {

    private static final String EMPTY = "";
    private static final String SUCCESSFUL_TRANSFER_BODY = "{\"transferStatus\": \"OK\"}";
    private static final String FAILED_TRANSFER_BODY = "{\"transferStatus\": \"FAILED\"}";
    private static final String EXCEPTION_BODY = "{\"statusCode\": %d, \"message\": \"%s\"}";

    private final Gson gson;
    private final ClientAccountService clientAccountService;
    private final TransferService transferService;

    @Inject
    public RouterImpl(Gson gson, ClientAccountService clientAccountService, TransferService transferService) {
        this.gson = gson;
        this.clientAccountService = clientAccountService;
        this.transferService = transferService;
    }

    @Override
    public void start() {
        path("/api", () -> {
            before("/*", (req, res) -> {
                log.info("Received {} request to endpoint: {}", req.requestMethod(), req.uri());
                res.type("application/json");
            });

            path("/customers", () -> {

                get(EMPTY, (req, res) -> gson.toJsonTree(clientAccountService.getCustomers()));

                path("/:customerId", () -> {

                    get(EMPTY, (req, res) -> {
                        Long customerId = Long.valueOf(req.params("customerId"));
                        return gson.toJsonTree(clientAccountService.getCustomer(customerId));
                    });

                    path("/accounts", () -> {

                        get(EMPTY, (req, res) -> {
                            Long customerId = Long.valueOf(req.params("customerId"));
                            return gson.toJsonTree(clientAccountService.getAccounts(customerId));
                        });

                        get("/:accountId", (req, res) -> {
                            Long customerId = Long.valueOf(req.params("customerId"));
                            Long accountId = Long.valueOf(req.params("accountId"));
                            return gson.toJsonTree(clientAccountService.getAccount(customerId, accountId));
                        });
                    });
                });
            });

            post("/transfer", (req, res) -> {
                TransferDTO transfer = gson.fromJson(req.body(), TransferDTO.class);
                return transferService.transfer(transfer) ? SUCCESSFUL_TRANSFER_BODY : FAILED_TRANSFER_BODY;
            });

            exception(CustomerNotFoundException.class, (ex, req, res) -> {
                res.status(404);
                res.body(String.format(EXCEPTION_BODY, 404, ex.getMessage()));
            });

            exception(AccountNotFoundException.class, (ex, req, res) -> {
                int statusCode = "post".equalsIgnoreCase(req.requestMethod()) ? 400 : 404;
                res.status(statusCode);
                res.body(String.format(EXCEPTION_BODY, statusCode, ex.getMessage()));
            });

            exception(InsufficientFundsException.class, (ex, req, res) -> {
                res.status(400);
                res.body(String.format(EXCEPTION_BODY, 400, ex.getMessage()));
            });

            exception(InvalidAmountValueException.class, (ex, req, res) -> {
                res.status(400);
                res.body(String.format(EXCEPTION_BODY, 400, ex.getMessage()));
            });
        });
    }

}
