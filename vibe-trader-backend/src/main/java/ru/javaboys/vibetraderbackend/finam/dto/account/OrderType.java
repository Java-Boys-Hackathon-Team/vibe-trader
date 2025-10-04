package ru.javaboys.vibetraderbackend.finam.dto.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    ORDER_TYPE_UNSPECIFIED(0),
    ORDER_TYPE_MARKET(1),
    ORDER_TYPE_LIMIT(2),
    ORDER_TYPE_STOP(3),
    ORDER_TYPE_STOP_LIMIT(4),
    ORDER_TYPE_MULTI_LEG(5)
    ;

    private final Integer code;

}
