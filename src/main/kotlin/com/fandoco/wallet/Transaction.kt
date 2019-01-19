package com.fandoco.wallet

import org.joda.money.Money
import java.time.Duration
import java.time.LocalDate
import java.util.*

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
        description: String,
        val fromDate: LocalDate,
        val toDate: LocalDate,
        val frequency: Duration,
        amount: Money,
        fromAccount: Account,
        toAccount: Account
)