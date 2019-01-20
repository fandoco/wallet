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

        val prasadPBId = TransactionRepository.addAccount(prasadPostBank)
        val chanyaPBId =TransactionRepository.addAccount(chanyaPostBank)
        val rentalID =TransactionRepository.addAccount(rental)


        val accounts = TransactionRepository.getAllAccounts()
        accounts.forEach { println(it) }

        prasadPostBank = TransactionRepository.getAccount(prasadPBId)!!
        chanyaPostBank = TransactionRepository.getAccount(chanyaPBId)!!
        rental = TransactionRepository.getAccount(rentalID)!!

        val janRental = Transaction(thisMonth(27), "Jan rental", jpy(115000), prasadPostBank, rental)
        TransactionRepository.addTransaction(janRental)

    }
}