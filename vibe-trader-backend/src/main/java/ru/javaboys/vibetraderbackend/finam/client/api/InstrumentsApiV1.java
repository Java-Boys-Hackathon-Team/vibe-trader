package ru.javaboys.vibetraderbackend.finam.client.api;

import java.time.Instant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.javaboys.vibetraderbackend.finam.client.FinamClientConfiguration;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.BarsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.LasestOrderBookResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.LatestTradesResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.QuoteResponse;
import ru.javaboys.vibetraderbackend.finam.dto.instrument.TimeFrameType;

@FeignClient(
        name = "finam-instruments-api",
        url = "${finam-api.url}/v1/instruments",
        configuration = FinamClientConfiguration.class
)
public interface InstrumentsApiV1 {

    @GetMapping(value = "/{symbol}/bars")
    BarsResponse bars(
            @PathVariable("symbol") String symbol,
            @RequestParam("interval.start_time") Instant startTime,
            @RequestParam("interval.end_time") Instant endTime,
            @RequestParam("timeframe") TimeFrameType timeframe
    );

    @GetMapping(value = "/{symbol}/quotes/latest")
    QuoteResponse latestQuote(
            @PathVariable("symbol") String symbol
    );

    @GetMapping(value = "/{symbol}/trades/latest")
    LatestTradesResponse latestTrades(
            @PathVariable("symbol") String symbol
    );

    @GetMapping(value = "/{symbol}/orderbook")
    LasestOrderBookResponse latestOrderBook(
            @PathVariable("symbol") String symbol
    );

}
