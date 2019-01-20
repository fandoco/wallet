package com.fandoco.wallet

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletTest {

    @Test
    fun testWallet() {
        val prasadPostBank = Account("Prasad Post Bank", JPY, Balance(thisMonth(1), jpy(300000)))
        val chanyaPostBank = Account("Chanya Post Bank", JPY, Balance(thisMonth(1), jpy(35000)))
        val chanyaMizuho = Account("Chanya Mizuho", JPY, Balance(thisMonth(1), jpy(400)))
        val chanyaNorthPacific = Account("Chanya North Pacific", JPY, Balance(thisMonth(1), jpy(118000)))
        val prasadRakuten = Account("Prasad Rakuten", JPY, Balance(thisMonth(1), jpy(180000)))
//        val chanyaCommercial = Account("Chanya Commercial", JPY, Balance(thisMonth(1), lkr(5000)))
//        val prasadCommercial = Account("Prasad Commercial", JPY, Balance(thisMonth(1), lkr(85000)))
        val chanyaCashJpy = Account("Chanya Cash", JPY, Balance(thisMonth(1), jpy(10000)))
        val prasadCashJpy = Account("Prasad Cash", JPY, Balance(thisMonth(1), jpy(1000)))
        val rakutenCard = Account("Rakuten Card", JPY, Balance(thisMonth(1), jpy(-180000)))
        val amexCard = Account("Amex Card", JPY, Balance(thisMonth(1), jpy(-180000)))
        val ts3Card = Account("Ts3 Card", JPY, Balance(thisMonth(1), jpy(-135000)))
        val rental = Account("Rental", JPY, Balance(thisMonth(1), jpy(0)))
        val car = Account("Car", JPY, Balance(thisMonth(1), jpy(0)))
        val electricity = Account("Electricity", JPY, Balance(thisMonth(1), jpy(0)))
        val water = Account("Water", JPY, Balance(thisMonth(1), jpy(0)))
        val gas = Account("Gas", JPY, Balance(thisMonth(1), jpy(0)))
        val au = Account("Au", JPY, Balance(thisMonth(1), jpy(0)))
        val docomo = Account("Docomo", JPY, Balance(thisMonth(1), jpy(0)))
        val trasfersToSriLanka = Account("Transfer to Sri Lanka", JPY, Balance(thisMonth(1), jpy(0)))
        val otherExpences = Account("Other expences", JPY, Balance(thisMonth(1), jpy(0)))
        val food = Account("Food", JPY, Balance(thisMonth(1), jpy(0)))
        val fuel = Account("Fuel", JPY, Balance(thisMonth(1), jpy(0)))
//        val trasfersFromJapan = Account("Transfer from Japan", LKR, Balance(thisMonth(1), lkr(0)))

        val janRental = Transaction(thisMonth(27), "Jan rental", jpy(115000), prasadPostBank, rental)
        val janTs3 = Transaction(thisMonth(2), "Jan Ts3", jpy(60000), chanyaNorthPacific, ts3Card)
        val janCar = Transaction(thisMonth(2), "Jan Car", jpy(45000), chanyaNorthPacific, car)
        val janAmex = Transaction(thisMonth(10), "Jan Amex", jpy(170000), prasadPostBank, amexCard)
        val janRakuten = Transaction(thisMonth(27), "Jan Rakuten", jpy(15000), prasadRakuten, rakutenCard)
        val janToSriLanka = Transaction(thisMonth(27), "Jan to Sri Lanka", jpy(75000), prasadPostBank, trasfersToSriLanka)

        val janElectricity = Transaction(thisMonth(27), "Jan electricity", jpy(5000), amexCard, electricity)
        val janWater = Transaction(thisMonth(29), "Jan water", jpy(3000), amexCard, water)
        val janGas = Transaction(thisMonth(15), "Jan gas", jpy(9000), amexCard, gas)
        val janAu = Transaction(thisMonth(30), "Jan Au", jpy(12000), amexCard, au)
        val janDocomo = Transaction(thisMonth(30), "Jan Docomo", jpy(14000), amexCard, docomo)

        val food1 = Transaction(thisMonth(7), "Food", jpy(7500), amexCard, food)
        val food2 = Transaction(thisMonth(14), "Food", jpy(5600), amexCard, food)
        val food3 = Transaction(thisMonth(21), "Food", jpy(3222), amexCard, food)
        val food4 = Transaction(thisMonth(28), "Food", jpy(3566), amexCard, food)

        val transactions = ArrayList<Transaction>()
        transactions.add(janRental)
        transactions.add(janTs3)
        transactions.add(janCar)
        transactions.add(janAmex)
        transactions.add(janRakuten)
        transactions.add(janToSriLanka)
        transactions.add(janElectricity)
        transactions.add(janWater)
        transactions.add(janGas)
        transactions.add(janAu)
        transactions.add(janDocomo)
        transactions.add(food1)
        transactions.add(food2)
        transactions.add(food3)
        transactions.add(food4)

        val accountViews = TransactionProcessor.process(transactions)

        accountViews.forEach { accountView ->
            println()
            println(accountView.account.name)
            println()
            println("Starting balance  - ${accountView.account.balance.amount}")
            var endBalance = accountView.account.balance.amount
            accountView.entries.forEach {entry ->
                endBalance = endBalance.plus(entry.amount)
                println("    ${entry.date}    ${entry.description}    ${entry.amount}")
            }
            println("End balance - $endBalance")
        }


    }

    @Test
    fun testRecEntriesForThePeriod() {
        val amexCard = Account("Amex Card", JPY, Balance(thisMonth(1), jpy(-180000)))
        val electricity = Account("Electricity", JPY, Balance(thisMonth(1), jpy(0)))

        val periodStart = LocalDate.of(2018, 12, 25)
        val periodEnd = LocalDate.of(2019, 10, 20)
        val recurringTransaction = RecurringTransaction(
                "Electricity",
                LocalDate.of(2018, 1, 31),
                LocalDate.MAX,
                Monthly(2),
                jpy(3000),
                amexCard,
                electricity
        )

        val entries = recEntriesForThePeriod(periodStart, periodEnd, recurringTransaction)
        println(entries)
    }
}

fun thisMonth(date: Int): LocalDate {
    return date(2019, 1, date)
}