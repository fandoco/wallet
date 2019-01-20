package com.fandoco.wallet

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

data class Account constructor(val id: UUID, val name: String, val currency: CurrencyUnit, val balance: Balance) {
    constructor(name: String, currency: CurrencyUnit, balance: Balance) : this(UUID.randomUUID(), name, currency, balance)
}

data class AccountView constructor(val account: Account) {
    val entries: ArrayList<Entry> = ArrayList()

    fun addEntry(entry: Entry) {
        entries.add(entry)
    }
}

data class Entry(val id: UUID, val transactionId: UUID, val date: LocalDate, val description: String, val amount: Money) {
    constructor(
            transactionId: UUID,
            date: LocalDate,
            description: String,
            amount: Money
    ) : this(UUID.randomUUID(), transactionId, date, description, amount)
}

data class Balance(val date: LocalDate, val amount: Money)

data class FutureEntry(val date: LocalDate, val description: String, val amount: Money)