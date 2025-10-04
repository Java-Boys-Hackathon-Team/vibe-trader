package ru.javaboys.vibetraderbackend.agent.tools;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ru.javaboys.vibetraderbackend.finam.client.api.AssetsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.asset.Asset;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetOptionResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetParamResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetScheduleResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.AssetsResponse;
import ru.javaboys.vibetraderbackend.finam.dto.asset.ClockResponse;

@Component
@RequiredArgsConstructor
public class AssetsServiceTools {

    private final AssetsApiV1 assets;
    private final ObjectMapper om;

    @Tool(description = """
            Возвращает список доступных инструментов, их описание.
            """)
    public AssetsResponse getAccount(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Описание инструмента
                    """) String tool
    ) {
        List<Asset> list = assets.assets().getAssets().stream()
                .filter(a -> {
                    try {
                        return om.writeValueAsString(a).contains(tool);
                    } catch (JsonProcessingException e) {
                        return false;
                    }
                })
                .toList();

        return AssetsResponse.builder().assets(list).build();
    }

    @Tool(description = """
            Возвращает список доступных инструментов, их описание.
            """)
    public AssetResponse asset(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Символ инструмента.
                    """) String symbol,
            @ToolParam(description = """
                    Идентификатор счёта.
                    """) String accountId
    ) {
        return assets.asset(symbol, accountId);
    }

    @Tool(description = """
            Получение торговых параметров по инструменту.
            """)
    public AssetParamResponse param(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Символ инструмента.
                    """) String symbol,
            @ToolParam(description = """
                    Идентификатор счёта.
                    """) String accountId
    ) {
        return assets.param(symbol, accountId);
    }

    @Tool(description = """
            Получение цепочки опционов для базового актива.
            """)
    public AssetOptionResponse option(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Символ инструмента.
                    """) String symbol
    ) {
        return assets.option(symbol);
    }

    @Tool(description = """
            Получение расписания торгов для инструмента.
            """)
    public AssetScheduleResponse schedule(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid,
            @ToolParam(description = """
                    Символ инструмента.
                    """) String symbol
    ) {
        return assets.schedule(symbol);
    }

    @Tool(description = """
            Получение времени на сервере.
            """)
    public ClockResponse clock(
            @ToolParam(description = """
                    Служебный UID запроса; передай как есть, строкой.
                    """) String promptUid
    ) {
        return assets.clock();
    }

}
