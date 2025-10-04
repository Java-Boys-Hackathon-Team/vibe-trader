package ru.javaboys.vibetraderbackend.agent.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import ru.javaboys.vibetraderbackend.finam.client.api.ExchangesApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.exchange.ExchangesResponse;

@Component
@RequiredArgsConstructor
public class ExchangesServiceTools {

    private final ExchangesApiV1 exchangesApi;

    @Tool(description = """
            Получает список доступных бирж с их названиями и mic-кодами.
            Используй этот инструмент, когда нужно узнать, какие биржи поддерживаются Finam API.
            Вход: promptUid: служебный идентификатор запроса 
            Выход: объект, содержащий массив бирж (exchanges).
            Каждая биржа имеет:
              - mic (строка) — идентификатор биржи (MIC код);
              - name (строка) — наименование биржи.
            """)
    public ExchangesResponse getExchanges(@ToolParam(description = "Служебный UID запроса; передай как есть, строкой") String promptUid) {
        return exchangesApi.exchanges();
    }

}
