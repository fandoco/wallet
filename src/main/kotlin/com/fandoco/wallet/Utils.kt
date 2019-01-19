package com.fandoco.wallet

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDate

val LKR = CurrencyUnit.of("LKR")
val JPY = CurrencyUnit.JPY

fun jpy(value: Long): Money {
    return Money.of(JPY, BigDecimal.valueOf(value))
}

fun lkr(value: Long): Money {
    return Money.of(LKR, value.toDouble())
}

fun date(year: Int, month: Int, date: Int): LocalDate {
    return LocalDate.of(year, month, date)
}