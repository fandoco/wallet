package com.fandoco.wallet

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object TransactionProcessor {
    fun process(transactions: List<Transaction>) : List<AccountView> {
        val accountViews = HashMap<UUID, AccountView>()

        transactions.forEach {
            var fromAccount = accountViews[it.from.id]

            if (fromAccount == null) {
                fromAccount = AccountView(it.from)
                accountViews[it.from.id] = fromAccount
            }

            var toAccount = accountViews[it.to.id]

            if (toAccount == null) {
                toAccount = AccountView(it.to)
                accountViews[it.to.id] = toAccount
            }

            val fromEntry = Entry(it.id, it.date, it.description, it.amount.negated())
            val toEntry = Entry(it.id, it.date, it.description, it.amount)

            fromAccount.addEntry(fromEntry)
            toAccount.addEntry(toEntry)
        }

        return ArrayList(accountViews.values)
    }
}