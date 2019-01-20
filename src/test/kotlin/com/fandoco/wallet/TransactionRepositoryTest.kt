package com.fandoco.wallet

import org.junit.Test

class TransactionRepositoryTest {

    @Test
    fun testRepository() {

        TransactionRepository.deleteAll()

        // Add account
        var prasadPostBank = Account("Prasad Post Bank", JPY, Balance(thisMonth(3), jpy(300000)))
        var chanyaPostBank = Account("Chanya Post Bank", JPY, Balance(thisMonth(15), jpy(35000)))
        var rental = Account("Rental", JPY, Balance(thisMonth(1), jpy(0)))
        var gas = Account("Gas", JPY, Balance(thisMonth(1), jpy(0)))

        val prasadPBId = TransactionRepository.addAccount(prasadPostBank)
        val chanyaPBId =TransactionRepository.addAccount(chanyaPostBank)
        val rentalId =TransactionRepository.addAccount(rental)
        val gasId =TransactionRepository.addAccount(gas)


        val accounts = TransactionRepository.getAllAccounts()
        accounts.forEach { println(it) }

        prasadPostBank = TransactionRepository.getAccount(prasadPBId)!!
        chanyaPostBank = TransactionRepository.getAccount(chanyaPBId)!!
        rental = TransactionRepository.getAccount(rentalId)!!
        gas = TransactionRepository.getAccount(gasId)!!

        val janRental = Transaction(thisMonth(27), "Jan rental", jpy(115000), prasadPostBank, rental)
        val janGas = Transaction(thisMonth(15), "Jan gas", jpy(9000), chanyaPostBank, gas)

        TransactionRepository.addTransaction(janRental)
        TransactionRepository.addTransaction(janGas)

        val transactions = TransactionRepository.getAllTransactions()
        transactions.forEach { println(it) }

        println("\nChanya Post Bank transactions : ${TransactionRepository.getTransactions(chanyaPostBank)}\n")
        println("\nPrasad Post Bank transactions : ${TransactionRepository.getTransactions(prasadPostBank)}\n")
        println("\nRental transactions : ${TransactionRepository.getTransactions(rental)}\n")
        println("\nGas transactions : ${TransactionRepository.getTransactions(gas)}\n")
    }
}