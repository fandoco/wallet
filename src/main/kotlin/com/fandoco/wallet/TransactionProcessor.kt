package com.fandoco.wallet

import java.util.*
import kotlin.collections.HashMap

object TransactionProcessor {
    fun process(transactions: List<Transaction>) {
        val accounts = HashMap<UUID, Account>()

        transactions.forEach {
            var fromAccount = accounts[it.from.id]

            if (fromAccount == null) {
                fromAccount = it.from
                accounts[it.from.id] = fromAccount
            }

            var toAccount = accounts[it.to.id]

            if (toAccount == null) {
                toAccount = it.to
                accounts[it.to.id] = toAccount
            }

            val fromEntry = Entry(it.id, it.date, it.description, it.amount.negated())
            val toEntry = Entry(it.id, it.date, it.description, it.amount)

            fromAccount.addEntry(fromEntry)
            toAccount.addEntry(toEntry)
        }

        accounts.forEach {
            println("${it.value.name} - ${it.value.balance.amount}")
            var endBalance = it.value.balance.amount
            it.value.entries.forEach {
                endBalance = endBalance.plus(it.amount)
                println("----------- ${it.date} ------------- ${it.description} -------- ${it.amount}")
            }
            println("Balance of Account ${it.value.name} is ${endBalance.amount}")

        }

    }
}