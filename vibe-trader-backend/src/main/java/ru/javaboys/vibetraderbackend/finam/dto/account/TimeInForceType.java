package ru.javaboys.vibetraderbackend.finam.dto.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeInForceType {
    TIME_IN_FORCE_UNSPECIFIED(0),
    TIME_IN_FORCE_DAY(1),
    TIME_IN_FORCE_GOOD_TILL_CANCEL(2),
    TIME_IN_FORCE_GOOD_TILL_CROSSING(3),
    TIME_IN_FORCE_EXT(4),
    TIME_IN_FORCE_ON_OPEN(5),
    TIME_IN_FORCE_ON_CLOSE(6),
    TIME_IN_FORCE_IOC(7),
    TIME_IN_FORCE_FOK(8)
    ;

    private final Integer code;

}
