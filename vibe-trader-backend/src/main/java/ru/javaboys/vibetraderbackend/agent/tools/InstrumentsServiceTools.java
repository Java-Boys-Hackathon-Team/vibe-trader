package ru.javaboys.vibetraderbackend.agent.tools;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.javaboys.vibetraderbackend.finam.client.api.InstrumentsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.BarsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.LatestTrade;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.LatestTradesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.QuoteResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.TimeFrameType;

@Component
@RequiredArgsConstructor
public class InstrumentsServiceTools {

    private final InstrumentsApiV1 instrumentsApi;

    @Tool(description = """
        Получает исторические данные (свечи) по инструменту.
        Используй этот инструмент, когда нужно получить OHLCV-данные за период.

        Вход:
          - promptUid: служебный идентификатор запроса
          - symbol: тикер инструмента (например, SBER, GAZP, AAPL);
          - timeframe: таймфрейм (строкой): TIME_FRAME_M1 | TIME_FRAME_M5 | TIME_FRAME_M15 | TIME_FRAME_M30 |
                       TIME_FRAME_H1 | TIME_FRAME_H2 | TIME_FRAME_H4 | TIME_FRAME_H8 |
                       TIME_FRAME_D | TIME_FRAME_W | TIME_FRAME_MN | TIME_FRAME_QR
          - startTime: начало периода (ISO-8601, например 2024-01-01T00:00:00Z);
          - endTime:   конец периода  (ISO-8601, например 2024-01-31T23:59:59Z);
        
        Выход (BarsResponse):
          - symbol (string) — символ инструмента;
          - bars (array of Bar) — массив агрегированных свечей, где каждая Bar содержит:
              • timestamp (string) — метка времени свечи;
              • open      (string) — цена открытия;
              • high      (string) — максимальная цена;
              • low       (string) — минимальная цена;
              • close     (string) — цена закрытия;
              • volume    (string) — объём за свечу в штуках.
        """)
    public BarsResponse getBars(
            @ToolParam(description = "Служебный UID запроса; передай как есть, строкой") String promptUid,
            @ToolParam(description = "Тикер инструмента, например: SBER, GAZP, AAPL") String symbol,
            @ToolParam(description = "Таймфрейм строкой, например: TIME_FRAME_M1 или TIME_FRAME_D") String timeframe,
            @ToolParam(description = "Начало периода в ISO-8601, например 2024-01-01T00:00:00Z") Instant startTime,
            @ToolParam(description = "Конец периода в ISO-8601, например 2024-01-31T23:59:59Z") Instant endTime
    ) {
        final TimeFrameType tf;
        try {
            tf = TimeFrameType.valueOf(timeframe);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Неверный таймфрейм: " + timeframe +
                            ". Используй одно из: " + Arrays.toString(TimeFrameType.values())
            );
        }
        return instrumentsApi.bars(symbol, startTime, endTime, tf);
    }

    @Tool(description = """
        Получает последнюю котировку по инструменту.

        Вход:
          - promptUid: служебный идентификатор запроса
          - symbol: тикер инструмента.

        Выход (QuoteResponse):
          - symbol (string) — символ инструмента;
          - quote (object) — последняя котировка, содержит:
              • symbol     (string) — символ инструмента;
              • timestamp  (string) — метка времени;
              • ask        (string) — лучшая цена продажи (0 при отсутствии);
              • ask_size   (string) — размер по ask;
              • bid        (string) — лучшая цена покупки (0 при отсутствии);
              • bid_size   (string) — размер по bid;
              • last       (string) — цена последней сделки;
              • last_size  (string) — размер последней сделки;
              • volume     (string) — дневной объём сделок;
              • turnover   (string) — дневной оборот;
              • open       (string) — дневная цена открытия;
              • high       (string) — дневной максимум;
              • low        (string) — дневной минимум;
              • close      (string) — дневная цена закрытия;
              • change     (string) — изменение (last - close);
              • option     (object, опционально) — доп. инфо по опциону (если применимо).
        """)
    public QuoteResponse getLatestQuote(
            @ToolParam(description = "Служебный UID запроса; передай как есть, строкой") String promptUid,
            @ToolParam(description = "Тикер инструмента, например: SBER, GAZP, AAPL") String symbol
    ) {
        return instrumentsApi.latestQuote(symbol);
    }

    @Tool(description = """
        Получает список последних сделок по инструменту (tape).

        Вход:
          - promptUid: служебный идентификатор запроса
          - symbol: тикер инструмента.

        Выход (LatestTradesResponse):
          - symbol (string) — символ инструмента;
          - trades (array of Trade) — последние сделки, где каждая Trade содержит:
              • trade_id  (string) — идентификатор сделки у биржи;
              • mpid      (string) — идентификатор участника рынка;
              • timestamp (string) — метка времени сделки;
              • price     (string) — цена сделки;
              • size      (string) — размер сделки;
              • side      (enum)   — сторона сделки: buy или sell.
        """)
    public LatestTradesResponse getLatestTrades(
            @ToolParam(description = "Служебный UID запроса; передай как есть, строкой") String promptUid,
            @ToolParam(description = "Тикер инструмента, например: SBER, GAZP, AAPL") String symbol
    ) {
        LatestTradesResponse tradesResponse = instrumentsApi.latestTrades(symbol);
        List<LatestTrade> list = tradesResponse.getTrades().stream()
                .limit(20)
                .toList();
        return LatestTradesResponse.builder()
                .symbol(tradesResponse.getSymbol())
                .trades(list)
                .build();
    }

//    @Tool(description = """
//        Получает текущий стакан (OrderBook) по инструменту.
//
//        Вход:
//          - promptUid: служебный идентификатор запроса
//          - symbol: тикер инструмента.
//
//        Выход (LasestOrderBookResponse):
//          - symbol   (string) — символ инструмента;
//          - orderbook (object) — стакан:
//              • rows (array of Row) — уровни стакана (bid/ask). Как минимум содержат цену и объём
//                по каждой стороне. В типовых схемах у Row есть поля вроде side (bid/ask), price, size/quantity.
//        """)
//    public LasestOrderBookResponse getOrderBook(
//            @ToolParam(description = "Служебный UID запроса; передай как есть, строкой") String promptUid,
//            @ToolParam(description = "Тикер инструмента, например: SBER, GAZP, AAPL") String symbol
//    ) {
//        return instrumentsApi.latestOrderBook(symbol);
//    }
}