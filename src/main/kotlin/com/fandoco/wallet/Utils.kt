package com.fandoco.wallet

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.joda.time.DateTime
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset

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

fun creditCardRemainingBalance(currentBalance: Money, creditLimit: Money, recurringPaymentAmounts: List<FutureEntry>): Money {
    var balanceOnPaymentDate = currentBalance

    recurringPaymentAmounts.forEach {
        balanceOnPaymentDate = balanceOnPaymentDate.plus(it.amount)
    }

    return creditLimit.minus(balanceOnPaymentDate)
}

fun recurringPaymentAmountsForPeriod(from: LocalDate, to: LocalDate, recTransactions: List<RecurringTransaction>) {
    val recPaymentAmounts = ArrayList<FutureEntry>()

    recTransactions.forEach { transaction ->
        recPaymentAmounts.addAll(recEntriesForThePeriod(from, to, transaction))
    }

}

fun recEntriesForThePeriod(periodStart: LocalDate, periodEnd: LocalDate, transaction: RecurringTransaction): List<FutureEntry> {

    val entries = ArrayList<FutureEntry>()

    val occurrences = transaction.frequency.getOccurrences(transaction.fromDate, transaction.toDate, periodStart, periodEnd)

    occurrences.forEach {
        entries.add(FutureEntry(it, transaction.description, transaction.amount))
    }

    return entries
}

fun toDateTime(localDate: LocalDate): DateTime {
    return DateTime(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
}

fun toLocalDate(dateTime: DateTime): LocalDate {
    return LocalDate.of(
            dateTime.year,
            dateTime.monthOfYear,
            dateTime.dayOfMonth
    )
}

fun toBalance(currency: String, dateTime: DateTime, amount: BigDecimal): Balance {
    return Balance(toLocalDate(dateTime), toMoney(currency, amount))
}

fun toMoney(currency: String, amount: BigDecimal): Money {
    return Money.of(CurrencyUnit.of(currency), amount.setScale(CurrencyUnit.of(currency).decimalPlaces))
}
