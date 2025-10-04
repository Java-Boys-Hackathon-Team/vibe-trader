package ru.javaboys.vibetraderbackend.agent.tools;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.javaboys.vibetraderbackend.finam.client.TokenInfoHolder;
import ru.javaboys.vibetraderbackend.finam.client.api.AccountsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;
import ru.javaboys.vibetraderbackend.finam.dto.account.AccountResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderLeg;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderStateResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderType;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrdersResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.PlaceOrderRequest;
import ru.javaboys.vibetraderbackend.finam.dto.account.StopConditionType;
import ru.javaboys.vibetraderbackend.finam.dto.account.TimeInForceType;
import ru.javaboys.vibetraderbackend.finam.dto.account.ValidBeforeType;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsResponse;

@Component
@RequiredArgsConstructor
public class AccountsServiceTools {

    private final TokenInfoHolder tokenInfoHolder;
    private final AccountsApiV1 accounts;

    @Tool(description = """
            Возвращает идентификатор счёта.
            """)
    public String getAccountId() {
        TokenDetailsResponse tokenInfo = tokenInfoHolder.getTokenInfo();
        if (tokenInfo != null) {
            try {
                return tokenInfo.getAccountIds().getFirst();
            } catch (Exception e) {
                return "Счёт отсутствует";
            }
        }

        return "Счёт отсутствует";
    }

    @Tool(description = """
            Возвращает полную информацию по торговому счёту.
            Используй, когда нужно получить детальную информацию по счёту.
            """)
    public AccountResponse getAccountById(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Идентификатор счёта.
                    """) String accountId
    ) {
        return accounts.account(accountId);
    }

//    На данный момент эти тулы не используется, так как API для демо счета не возвращает данные
//    @Tool(description = """
//            Возвращает историю сделок по счёту за указанный интервал.
//            Передавай оба края интервала в формате ISO-8601 с суффиксом Z (UTC), например: 2025-09-01T00:00:00Z.
//            """)
//    public TradesResponse trades(
//            @ToolParam(description = """
//                    Служебный UID запроса; передай как есть, строкой.
//                    """) String promptUid,
//            @ToolParam(description = """
//                    Идентификатор счёта.
//                    """)
//            String accountId,
//            @ToolParam(description = """
//                    Начало интервала в формате ISO-8601 UTC. Пример: "2025-09-01T00:00:00Z".
//                    """)
//            String intervalStartTimeIso,
//            @ToolParam(description = """
//                    Окончание интервала в формате ISO-8601 UTC. Пример: "2025-09-30T23:59:59Z".
//                    Должно быть не раньше значения начала интервала.
//                    """)
//            String intervalEndTimeIso,
//            @ToolParam(description = """
//                    Лимит количества записей (Long, > 0). Рекомендуемые значения: 100..1000 в зависимости от объёма истории.
//                    """)
//            Long limit
//    ) {
//        Instant start = Instant.parse(intervalStartTimeIso);
//        Instant end = Instant.parse(intervalEndTimeIso);
//        return accounts.trades(accountId, start, end, limit);
//    }
//
//    @Tool(description = """
//            Возвращает список транзакций по счёту за указанный интервал.
//            Передавай оба края интервала в формате ISO-8601 с Z (UTC), например: 2025-09-01T00:00:00Z.
//            """)
//    public TransactionsResponse transactions(
//            @ToolParam(description = """
//                    Служебный UID запроса; передай как есть, строкой.
//                    """) String promptUid,
//            @ToolParam(description = """
//                    Идентификатор счёта.
//                    """)
//            String accountId,
//            @ToolParam(description = """
//                    Начало интервала в формате ISO-8601 UTC. Пример: "2025-09-01T00:00:00Z".
//                    """)
//            String intervalStartTimeIso,
//            @ToolParam(description = """
//                    Окончание интервала в формате ISO-8601 UTC. Пример: "2025-09-30T23:59:59Z".
//                    """)
//            String intervalEndTimeIso
//    ) {
//        Instant start = Instant.parse(intervalStartTimeIso);
//        Instant end = Instant.parse(intervalEndTimeIso);
//        return accounts.transactions(accountId, start, end);
//    }

    @Tool(description = """
            Размещает новый торговый ордер по указанному счёту.
            Необходимо указать все обязательные параметры заявки.
            """)
    public OrderStateResponse placeOrder(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Идентификатор аккаунта.
                    """) String accountId,
            @ToolParam(description = """
                            Символ инструмента.
                            """) String symbol,
            @ToolParam(description = """
                            Количество в шт.
                            """) BigDecimal quantity,
            @ToolParam(description = """
                            Сторона (long или short).
                            """) String side,
            @ToolParam(description = """
                            Тип заявки.
                            """) OrderType type,
            @ToolParam(description = """
                            Срок действия заявки.
                            """) TimeInForceType timeInForce,
            @ToolParam(description = """
                            Необходимо для лимитной и стоп лимитной заявки.
                            """) BigDecimal limitPrice,
            @ToolParam(description = """
                            Необходимо для стоп рыночной и стоп лимитной заявки.
                            """) BigDecimal stopPrice,
            @ToolParam(description = """
                            Необходимо для стоп рыночной и стоп лимитной заявки.
                            """) StopConditionType stopCondition,
            @ToolParam(description = """
                            Необходимо для мульти лег заявки.
                            """) List<OrderLeg> legs,
            @ToolParam(description = """
                            Уникальный идентификатор заявки. Автоматически генерируется, если не отправлен. (максимум 20 символов).
                            """) String clientOrderId,
            @ToolParam(description = """
                            Срок действия условной заявки. Заполняется для заявок с типом ORDER_TYPE_STOP, ORDER_TYPE_STOP_LIMIT.
                            """) ValidBeforeType validBefore,
            @ToolParam(description = """
                            Метка заявки. (максимум 128 символов).
                            """) String comment
    ) {
        PlaceOrderRequest req = PlaceOrderRequest.builder()
                .accountId(accountId)
                .symbol(symbol)
                .quantity(BigDecimalValueWrapper.valueOf(quantity))
                .side(side)
                .type(type)
                .timeInForce(timeInForce)
                .limitPrice(BigDecimalValueWrapper.valueOf(limitPrice))
                .stopPrice(BigDecimalValueWrapper.valueOf(stopPrice))
                .stopCondition(stopCondition)
                .legs(legs)
                .clientOrderId(clientOrderId)
                .validBefore(validBefore)
                .comment(comment)
                .build();
        return accounts.placeOrder(accountId, req);
    }

    @Tool(description = """
            Возвращает состояние конкретного ордера по его идентификатору и счёту.
            """)
    public OrderStateResponse order(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Идентификатор счёта.
                    """)
            String accountId,
            @ToolParam(description = """
                    Идентификатор ордера (orderId), присвоенный системой при размещении.
                    """)
            String orderId
    ) {
        return accounts.order(accountId, orderId);
    }

    @Tool(description = """
            Возвращает список всех активных ордеров по счёту.
            """)
    public OrdersResponse orders(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Идентификатор счёта.
                    """)
            String accountId
    ) {
        return accounts.orders(accountId);
    }

    @Tool(description = """
            Отменяет ранее выставленный ордер по его идентификатору.
            Возвращает детальную информацию по ордеру.
            """)
    public OrderStateResponse cancelOrder(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Идентификатор счёта.
                    """)
            String accountId,
            @ToolParam(description = """
                    Идентификатор ордера, который нужно отменить.
                    """)
            String orderId
    ) {
        return accounts.cancelOrder(accountId, orderId);
    }
}