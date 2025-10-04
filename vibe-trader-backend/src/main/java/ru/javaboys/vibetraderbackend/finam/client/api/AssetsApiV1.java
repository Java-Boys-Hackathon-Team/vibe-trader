package ru.javaboys.vibetraderbackend.finam.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javaboys.vibetraderbackend.finam.client.FinamClientConfiguration;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetOptionResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetParamResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetScheduleResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.ClockResponse;

@FeignClient(
        name = "finam-assets-api",
        url = "${finam-api.url}/v1/assets",
        configuration = FinamClientConfiguration.class
)
public interface AssetsApiV1 {

    @GetMapping(value = "")
    AssetsResponse assets();

    @GetMapping(value = "/{symbol}")
    AssetResponse asset(
            @PathVariable("symbol") String symbol,
            @RequestParam("account_id") String accountId
    );

    @GetMapping(value = "/{symbol}/params")
    AssetParamResponse param(
            @PathVariable("symbol") String symbol,
            @RequestParam("account_id") String accountId
    );

    @GetMapping(value = "/{symbol}/options")
    AssetOptionResponse option(
            @PathVariable("symbol") String symbol
    );

    @GetMapping(value = "/{symbol}/schedule")
    AssetScheduleResponse schedule(
            @PathVariable("symbol") String symbol
    );

    @GetMapping(value = "/clock")
    ClockResponse clock();

}
