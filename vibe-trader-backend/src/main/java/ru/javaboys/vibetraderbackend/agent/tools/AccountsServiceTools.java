package ru.javaboys.vibetraderbackend.agent.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import ru.javaboys.vibetraderbackend.finam.client.api.AccountsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;
import ru.javaboys.vibetraderbackend.finam.dto.account.AccountResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrderStateResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.OrdersResponse;
import ru.javaboys.vibetraderbackend.finam.dto.account.PlaceOrderRequest;
import ru.javaboys.vibetraderbackend.finam.dto.trade.TradesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.transaction.TransactionsResponse;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AccountsServiceTools {

    private final AccountsApiV1 accounts;

    @Tool(description = """
            Возвращает полную информацию по торговому счёту.
            Используй, когда нужно получить сводку по счёту: базовые атрибуты, позиции, денежные средства, портфели (если применимо).
            Вход: точный идентификатор счёта.
            Выход: AccountResponse.
            """)
    public AccountResponse getAccount(
            @ToolParam(description = """
                    Идентификатор счёта. Строка без пробелов, например: "1899011".
                    """)
            String accountId
    ) {
        return accounts.account(accountId);
    }

    @Tool(description = """
            Возвращает историю сделок по счёту за указанный интервал.
            Передавай оба края интервала в формате ISO-8601 с суффиксом Z (UTC), например: 2025-09-01T00:00:00Z.
            Параметры: accountId, interval.start_time, interval.end_time, limit.
            Выход: TradesResponse.
            """)
    public TradesResponse trades(
            @ToolParam(description = """
                    Идентификатор счёта. Пример: "1899011".
                    """)
            String accountId,
            @ToolParam(description = """
                    Начало интервала в формате ISO-8601 UTC. Пример: "2025-09-01T00:00:00Z".
                    """)
            String intervalStartTimeIso,
            @ToolParam(description = """
                    Окончание интервала в формате ISO-8601 UTC. Пример: "2025-09-30T23:59:59Z".
                    Должно быть не раньше значения начала интервала.
                    """)
            String intervalEndTimeIso,
            @ToolParam(description = """
                    Лимит количества записей (Long, > 0). Рекомендуемые значения: 100..1000 в зависимости от объёма истории.
                    """)
            Long limit
    ) {
        Instant start = Instant.parse(intervalStartTimeIso);
        Instant end = Instant.parse(intervalEndTimeIso);
        return accounts.trades(accountId, start, end, limit);
    }

    @Tool(description = """
            Возвращает список транзакций по счёту (пополнения/выводы, комиссии, налоги, торговые операции и пр.) за указанный интервал.
            Передавай оба края интервала в формате ISO-8601 с Z (UTC), например: 2025-09-01T00:00:00Z.
            Параметры: accountId, interval.start_time, interval.end_time.
            Выход: TransactionsResponse.
            """)
    public TransactionsResponse transactions(
            @ToolParam(description = """
                    Идентификатор счёта. Пример: "1899011".
                    """)
            String accountId,
            @ToolParam(description = """
                    Начало интервала в формате ISO-8601 UTC. Пример: "2025-09-01T00:00:00Z".
                    """)
            String intervalStartTimeIso,
            @ToolParam(description = """
                    Окончание интервала в формате ISO-8601 UTC. Пример: "2025-09-30T23:59:59Z".
                    """)
            String intervalEndTimeIso
    ) {
        Instant start = Instant.parse(intervalStartTimeIso);
        Instant end = Instant.parse(intervalEndTimeIso);
        return accounts.transactions(accountId, start, end);
    }

    @Tool(description = """
            Размещает новый торговый ордер по указанному счёту.
            Необходимо указать все обязательные параметры заявки: символ инструмента, направление (BUY/SELL),
            количество, цену (для лимитного ордера), срок действия (time_in_force/valid_before) и при необходимости client_order_id.
            Выход: OrderStateResponse с текущим состоянием ордера.
            """)
    public OrderStateResponse placeOrder(
            @ToolParam(description = """
                    Идентификатор счёта, на который выставляется ордер. Пример: "1899011".
                    """)
            String accountId,
            @ToolParam(description = """
                    Код инструмента (symbol), например "SBER_TQBR".
                    """)
            String symbol,
            @ToolParam(description = """
                    Направление ордера: BUY или SELL.
                    """)
            String side,
            @ToolParam(description = """
                    Количество лотов. Целое число > 0.
                    """)
            Long quantity,
            @ToolParam(description = """
                    Цена за единицу. Для рыночных ордеров можно не указывать.
                    """)
            Double price,
            @ToolParam(description = """
                    Срок действия ордера. Возможные значения: GTC (Good Till Cancel), DAY (до конца дня), IOC, FOK.
                    """)
            String timeInForce,
            @ToolParam(description = """
                    Метка клиента (client_order_id). Строка, уникальная в пределах аккаунта, для идемпотентности.
                    Необязательный параметр.
                    """)
            String clientOrderId,
            @ToolParam(description = """
                    Время, до которого ордер активен, если поддерживается. Формат ISO-8601 UTC.
                    Необязательный параметр.
                    """)
            String validBeforeIso
    ) {
        PlaceOrderRequest req = PlaceOrderRequest.builder()
                .symbol(symbol)
                .side(side)
                .quantity(BigDecimalValueWrapper.valueOf(quantity))
//                .price(price)
//                .timeInForce(timeInForce)
                .clientOrderId(clientOrderId)
//                .validBefore(validBeforeIso != null ? Instant.parse(validBeforeIso) : null)
                .build();
        return accounts.placeOrder(accountId, req);
    }

    @Tool(description = """
            Возвращает состояние конкретного ордера по его идентификатору и счёту.
            Выход: OrderStateResponse с полями статуса, исполненного количества, оставшегося количества и др.
            """)
    public OrderStateResponse order(
            @ToolParam(description = """
                    Идентификатор счёта. Пример: "1899011".
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
            Выход: OrdersResponse — массив ордеров с их текущим состоянием.
            """)
    public OrdersResponse orders(
            @ToolParam(description = """
                    Идентификатор счёта. Пример: "1899011".
                    """)
            String accountId
    ) {
        return accounts.orders(accountId);
    }

    @Tool(description = """
            Отменяет ранее выставленный ордер по его идентификатору.
            Выход: OrderStateResponse с новым состоянием (например, CANCELLED).
            """)
    public OrderStateResponse cancelOrder(
            @ToolParam(description = """
                    Идентификатор счёта. Пример: "1899011".
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