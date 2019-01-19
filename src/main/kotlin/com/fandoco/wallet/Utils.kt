package com.fandoco.wallet

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.BigDecimal
import java.time.Duration
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

fun creditCardRemainingBalance(currentBalance: Money, creditLimit: Money, recurringPaymentAmounts: List<PartialEntry>): Money {
    var balanceOnPaymentDate = currentBalance

    recurringPaymentAmounts.forEach {
        balanceOnPaymentDate = balanceOnPaymentDate.plus(it.amount)
    }

    return creditLimit.minus(balanceOnPaymentDate)
}

fun recurringPaymentAmountsForPeriod(from: LocalDate, to: LocalDate, recurringTransactions: List<RecurringTransaction>) {
    val recurringPaymentAmounts = ArrayList<PartialEntry>()

    recurringTransactions.forEach { transaction ->
        val start = transaction.fromDate
        val end = transaction.toDate
        val frequency = transaction.frequency

        if(end.isBefore(from) || start.isAfter(to)) {
            return
        }

        val realStart = if (from.isAfter(start)) from else start
        val realEnd = if (to.isAfter(end)) end else to

        var count = 0;

        if(start.equals(realStart)) {
            count++
        }

        val startToTo = (Duration.between(start, to).toMillis() / frequency.toMillis()).toInt()
        if(startToTo == 0) {
            return
        }

        val startToFrom = (Duration.between(start, to).toMillis() / frequency.toMillis()).toInt()




    }
}
