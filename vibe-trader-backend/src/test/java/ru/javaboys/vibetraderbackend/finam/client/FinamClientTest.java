package ru.javaboys.vibetraderbackend.finam.client;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestExecutionListeners;
import ru.javaboys.vibetraderbackend.finam.client.api.AccountsApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.AssetsApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.ExchangesApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.InstrumentsApiV1;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;
import ru.javaboys.vibetraderbackend.finam.dto.account.AccountResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderStateResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderType;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrdersResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.PlaceOrderRequest;
import ru.javaboys.vibetraderbackend.finam.dto.account.StopConditionType;
import ru.javaboys.vibetraderbackend.finam.dto.account.TimeInForceType;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetOptionResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetParamResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetScheduleResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.ClockResponse;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthResponse;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.exchange.ExchangesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.BarsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.LasestOrderBookResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.LatestTradesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.QuoteResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.TimeFrameType;
import ru.javaboys.vibetraderbackend.finam.dto.trade.TradesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.transaction.TransactionsResponse;
import ru.javaboys.vibetraderbackend.utils.DotenvTestExecutionListener;

//@SpringBootTest
@TestExecutionListeners(
        listeners = DotenvTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public class FinamClientTest {
    @Autowired private FinamClient finamClient;
    @Value("${finam-api.secret}") String secret;

    @Test
    @Disabled
    public void testClient() {

        boolean checkSessions = true;
        boolean checkAccounts = true;
        boolean checkAssets = true;
        boolean checkInstruments = true;
        boolean changeExchanges = true;

        String accountId = "TRQD05:431539";
        String symbol = "SBER@MISX";

        // Sessions API
        if (checkSessions) {
            SessionsApiV1 sessionsApi = finamClient.getSessionsApi();

            // https://api.finam.ru/v1/sessions
            AuthResponse authResponse = sessionsApi.auth(
                    AuthRequest.builder()
                            .secret(secret)
                            .build()
            );

            // https://api.finam.ru/v1/sessions/details
            TokenDetailsResponse tokenDetailsResponse = sessionsApi.tokenDetails(
                    TokenDetailsRequest.builder()
                            .token(authResponse.getToken())
                            .build()
            );

            int sessionsBreakpoint = 0;
        }

        // Accounts API
        if (checkAccounts) {
            AccountsApiV1 accountsApi = finamClient.getAccountsApi();

            // https://api.finam.ru/v1/accounts/{accountId}
            AccountResponse accountResponse = accountsApi.account(
                    accountId
            );

            // https://api.finam.ru/v1/accounts/{accountId}/trades
            try {
                TradesResponse tradesResponse = accountsApi.trades(
                        accountId,
                        Instant.now().minus(7, ChronoUnit.DAYS),
                        Instant.now(),
                        100L
                );
            } catch (Exception e) {
                // ignoring
            }

            // https://api.finam.ru/v1/accounts/{accountId}/transactions
            try {
                TransactionsResponse transactionsResponse = accountsApi.transactions(
                        accountId,
                        Instant.now().minus(7, ChronoUnit.DAYS),
                        Instant.now()
                );
            } catch (Exception e) {
                // ignoring
            }

            // https://api.finam.ru/v1/accounts/{accountId}/orders
            OrderStateResponse placeOrderResponse = accountsApi.placeOrder(
                    accountId,
                    PlaceOrderRequest.builder()
                            .symbol("F@XNYS")
                            .quantity(BigDecimalValueWrapper.valueOf(10))
                            .side("SIDE_BUY")
                            .type(OrderType.ORDER_TYPE_STOP_LIMIT)
                            .limitPrice(BigDecimalValueWrapper.valueOf(100))
                            .stopPrice(BigDecimalValueWrapper.valueOf(101))
                            .timeInForce(TimeInForceType.TIME_IN_FORCE_DAY)
                            .stopCondition(StopConditionType.STOP_CONDITION_LAST_UP)
                            .legs(List.of())
                            .clientOrderId(
                                    UUID.randomUUID()
                                            .toString()
                                            .replaceAll("-", "")
                                            .substring(0, 20)
                            )
                            .build()
            );
            String orderId = placeOrderResponse.getOrderId();

            // https://api.finam.ru/v1/accounts/{accountId}/orders/{orderId}
            OrderStateResponse orderResponse = accountsApi.order(
                    accountId,
                    orderId
            );

            // https://api.finam.ru/v1/accounts/{accountId}/orders
            OrdersResponse ordersResponse = accountsApi.orders(
                    accountId
            );

            // https://api.finam.ru/v1/accounts/{accountId}/orders/{orderId}
            OrderStateResponse closeOrderResponse = accountsApi.cancelOrder(
                    accountId,
                    orderId
            );

            int accountsBreakpoint = 0;
        }

        // Assets API
        if (checkAssets) {
            AssetsApiV1 assetsApi = finamClient.getAssetsApi();

            // https://api.finam.ru/v1/assets
            AssetsResponse assetsResponse = assetsApi.assets();

            // https://api.finam.ru/v1/assets/{symbol}
            AssetResponse assetResponse = assetsApi.asset(
                    symbol,
                    accountId
            );

            // https://api.finam.ru/v1/assets/{symbol}/params
            AssetParamResponse assetParamResponse = assetsApi.param(
                    symbol,
                    accountId
            );

            // https://api.finam.ru/v1/assets/{symbol}/options
            AssetOptionResponse assetOptionResponse = assetsApi.option(
                    symbol
            );

            // https://api.finam.ru/v1/assets/{symbol}/schedule
            AssetScheduleResponse assetScheduleResponse = assetsApi.schedule(
                    symbol
            );

            // https://api.finam.ru/v1/assets/clock
            ClockResponse clockResponse = assetsApi.clock();

            int assetsBreakpoint = 0;
        }

        // Instruments API
        if (checkInstruments) {
            InstrumentsApiV1 instrumentsApi = finamClient.getInstrumentsApi();

            // https://api.finam.ru/v1/instruments/{symbol}/bars
            BarsResponse barsResponse = instrumentsApi.bars(
                    symbol,
                    Instant.now().minus(7, ChronoUnit.DAYS),
                    Instant.now(),
                    TimeFrameType.TIME_FRAME_D
            );

            // https://api.finam.ru/v1/instruments/{symbol}/quotes/latest
            QuoteResponse quoteResponse = instrumentsApi.latestQuote(
                    symbol
            );

            // https://api.finam.ru/v1/instruments/{symbol}/trades/latest
            LatestTradesResponse tradesResponse = instrumentsApi.latestTrades(
                    symbol
            );

            // https://api.finam.ru/v1/instruments/{symbol}/orderbook
            LasestOrderBookResponse orderBookResponse = instrumentsApi.latestOrderBook(
                    symbol
            );

            int instrumentsBreakpoint = 0;
        }

        // Exchanges API
        if (changeExchanges) {
            ExchangesApiV1 exchangesApi = finamClient.getExchangesApi();

            // https://api.finam.ru/v1/exchanges
            ExchangesResponse exchangesResponse = exchangesApi.exchanges();

            int exchangesBreakpoint = 0;
        }

    }


}