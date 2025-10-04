package ru.javaboys.vibetraderbackend.finam.dto.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StopConditionType {
    STOP_CONDITION_UNSPECIFIED(0),
    STOP_CONDITION_LAST_UP(1),
    STOP_CONDITION_LAST_DOWN(2)
    ;

    private final Integer code;

}
