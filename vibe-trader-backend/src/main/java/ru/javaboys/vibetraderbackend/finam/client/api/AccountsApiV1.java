package ru.javaboys.vibetraderbackend.finam.client.api;

import java.time.Instant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ru.javaboys.vibetraderbackend.finam.client.FinamClientConfiguration;
import ru.javaboys.vibetraderbackend.finam.dto.account.AccountResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderStateResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrdersResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.PlaceOrderRequest;
import ru.javaboys.vibetraderbackend.finam.dto.trade.TradesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.transaction.TransactionsResponse;

@FeignClient(
        name = "finam-accounts-api",
        url = "${finam-api.url}/v1/accounts",
        configuration = FinamClientConfiguration.class
)
public interface AccountsApiV1 {

    @GetMapping(value = "/{accountId}")
    AccountResponse account(
            @PathVariable("accountId") String accountId
    );

    @Deprecated // Not working for demo accounts
    @GetMapping(value = "/{accountId}/trades")
    TradesResponse trades(
            @PathVariable("accountId") String accountId,
            @RequestParam("interval.start_time") Instant startTime,
            @RequestParam("interval.end_time") Instant endTime,
            @RequestParam("limit") Long limit
    );

    @Deprecated // Not working for demo accounts
    @GetMapping(value = "/{accountId}/transactions")
    TransactionsResponse transactions(
            @PathVariable("accountId") String accountId,
            @RequestParam("interval.start_time") Instant startTime,
            @RequestParam("interval.end_time") Instant endTime
    );

    @PostMapping(value = "/{accountId}/orders")
    OrderStateResponse placeOrder(
            @PathVariable("accountId") String accountId,
            @RequestBody PlaceOrderRequest request
    );

    @GetMapping(value = "/{accountId}/orders/{orderId}")
    OrderStateResponse order(
            @PathVariable("accountId") String accountId,
            @PathVariable("orderId") String orderId
    );

    @GetMapping(value = "/{accountId}/orders")
    OrdersResponse orders(
            @PathVariable("accountId") String accountId
    );

    @DeleteMapping(value = "/{accountId}/orders/{orderId}")
    OrderStateResponse cancelOrder(
            @PathVariable("accountId") String accountId,
            @PathVariable("orderId") String orderId
    );

}
