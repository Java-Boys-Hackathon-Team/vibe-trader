package ru.javaboys.vibetraderbackend.finam.dto.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionCategoryType {
    OTHERS(0),
    DEPOSIT(1),
    WITHDRAW(2),
    INCOME(5),
    COMMISSION(7),
    TAX(8),
    INHERITANCE(9),
    TRANSFER(11),
    CONTRACT_TERMINATION(12),
    OUTCOMES(13),
    FINE(15),
    LOAN(19)
    ;

    private final Integer code;

}
