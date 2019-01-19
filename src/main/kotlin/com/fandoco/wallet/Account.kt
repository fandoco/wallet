package com.fandoco.wallet

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class Account private constructor(val id: UUID, val name: String, val currency: CurrencyUnit, val balance: Balance) {
    constructor(name: String, currency: CurrencyUnit, balance: Balance) : this(UUID.randomUUID(), name, currency, balance)
}

class AccountView constructor(val account: Account) {
    val entries: ArrayList<Entry> = ArrayList()

    fun addEntry(entry: Entry) {
        entries.add(entry)
    }
}

class Entry(id: UUID, val transactionId: UUID, val date: LocalDate, val description: String, val amount: Money) {
    constructor(
            transactionId: UUID,
            date: LocalDate,
            description: String,
            amount: Money
    ) : this(UUID.randomUUID(), transactionId, date, description, amount)
}

class Balance(val date: LocalDate, val amount: Money)

class PartialEntry(val date: LocalDate, val amount: Money)