package ru.javaboys.vibetraderbackend.finam.dto.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidBeforeType {
    VALID_BEFORE_UNSPECIFIED(0),
    VALID_BEFORE_END_OF_DAY(1),
    VALID_BEFORE_GOOD_TILL_CANCEL(2),
    VALID_BEFORE_GOOD_TILL_DATE(3)
    ;

    private final Integer code;

}
