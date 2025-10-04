package ru.javaboys.vibetraderbackend.agent.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import ru.javaboys.vibetraderbackend.finam.client.api.SessionsApiV1;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthResponse;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsResponse;

@Component
@RequiredArgsConstructor
public class AuthServiceTools {

    private final SessionsApiV1 sessions;

    @Tool(description = """
            Получает сессионный JWT по API-токену (secret key).
            Используй этот инструмент, когда есть секрет из кабинета Trade API и нужно авторизоваться для последующих вызовов.
            Вход: secret (строка, ровно секрет без префиксов).
            Выход: объект с полем token (JWT). Дополнительно могут присутствовать служебные поля (например, время выдачи).
            """)
    public AuthResponse auth(
            @ToolParam(description = """
                    API токен (secret key) из кабинета. Это НЕ JWT.
                    Передавай значение секрета как есть, без 'Bearer ' и без пробелов.
                    Пример: fna_xxx_secret
                    """)
            String secret
    ) {
        AuthRequest req = AuthRequest.builder()
                .secret(secret)
                .build();
        return sessions.auth(req);
    }

    @Tool(description = """
            Возвращает атрибуты указанного JWT: время создания/истечения (ISO-8601 Z),
            права на маркет-дату (в т.ч. quote_level, delay_minutes, список mic),
            перечень доступных account_ids и признак readonly.
            Используй для проверки валидности токена и доступов перед торговыми/рыночными вызовами.
            Вход: token (строка JWT без 'Bearer ').
            Выход: структура с полями created_at, expires_at, md_permissions[], account_ids[], readonly и др.
            """)
    public TokenDetailsResponse tokenDetails(
            @ToolParam(description = """
                    JWT сессионного токена, полученный инструментом auth.
                    Передавай сам токен без префикса 'Bearer ' (формат: header.payload.signature).
                    """)
            String token
    ) {
        TokenDetailsRequest req = TokenDetailsRequest.builder()
                .token(token)
                .build();
        return sessions.tokenDetails(req);
    }
}
