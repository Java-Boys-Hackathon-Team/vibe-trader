package ru.javaboys.vibetraderbackend.finam.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuoteLevelType {
    QUOTE_LEVEL_UNSPECIFIED(0),
    QUOTE_LEVEL_LAST_PRICE(1),
    QUOTE_LEVEL_BEST_BID_OFFER(2),
    QUOTE_LEVEL_DEPTH_OF_MARKET(3),
    QUOTE_LEVEL_DEPTH_OF_BOOK(4),
    QUOTE_LEVEL_ACCESS_FORBIDDEN(5)
    ;

    private final Integer code;

}
