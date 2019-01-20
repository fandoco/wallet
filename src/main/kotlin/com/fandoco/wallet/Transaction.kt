package com.fandoco.wallet

import org.joda.money.Money
import java.lang.IllegalArgumentException
import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class Transaction private constructor(val id: UUID, val date: LocalDate, val description: String, val amount: Money, val from: Account, val to: Account) {
    constructor(
            date: LocalDate,
            description: String,
            amount: Money,
            from: Account,
            to: Account
    ) : this(UUID.randomUUID(), date, description, amount, from, to)
}

class RecurringTransaction(
        val description: String,
        val fromDate: LocalDate,
        val toDate: LocalDate,
        val frequency: Frequency,
        val amount: Money,
        val fromAccount: Account,
        val toAccount: Account
)

interface Frequency {
    fun getOccurrences(fromDate: LocalDate, toDate: LocalDate, periodStart: LocalDate, periodEnd: LocalDate) : List<LocalDate>
}

class Monthly(val count: Long) : Frequency {

    init {
        if(count < 1) {
            throw IllegalArgumentException("Count must by greater than 0.")
        }
    }

    override fun getOccurrences(fromDate: LocalDate, toDate: LocalDate, periodStart: LocalDate, periodEnd: LocalDate): List<LocalDate> {
        val occurrences = ArrayList<LocalDate>()

        if(fromDate.isAfter(periodEnd) || toDate.isBefore(periodStart)) {
            return emptyList()
        }

        var date = fromDate

        while (!date.isAfter(periodEnd)) {
            if(!date.isBefore(periodStart) && !date.isAfter(periodEnd)) {
                occurrences.add(date)
            }
            date = date.plusMonths(count)

            if(date.dayOfMonth != fromDate.dayOfMonth) {
                try {
                    date = LocalDate.of(date.year, date.month, fromDate.dayOfMonth)
                } catch (ignore: DateTimeException) {

                }
            }
        }

        return occurrences
    }
}