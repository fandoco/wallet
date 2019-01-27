package com.fandoco.wallet

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.money.CurrencyUnit
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

object TransactionRepository {
    init {
        Database.connect(dataSource())
    }

    fun deleteAll() {
        transaction {
            RecurringTransactionTable.deleteAll()
            TransactionTable.deleteAll()
            AccountTable.deleteAll()
        }
    }

    fun addAccount(account: Account): String {
        var id: String? = null
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            val entityId = AccountTable.insert { row ->
                row[AccountTable.name] = account.name
                row[AccountTable.currency] = account.currency.code
                row[AccountTable.reconDate] = toDateTime(account.balance.date)
                row[AccountTable.balance] = account.balance.amount.amount
            } get AccountTable.id

            id = entityId?.value.toString()

        }
        when (id) {
            null -> throw Exception("Error while trying to add account. {name: ${account.name}")
            else -> return id as String
        }
    }

    fun getAccount(id: String): Account? {
        var account: Account? = null
        transaction {
            addLogger(StdOutSqlLogger)

            val row = AccountTable.select { AccountTable.id eq UUID.fromString(id) }.singleOrNull()
            if (row != null) {
                account = Account(
                        row[AccountTable.id].value,
                        row[AccountTable.name],
                        CurrencyUnit.of(row[AccountTable.currency]),
                        toBalance(row[AccountTable.currency], row[AccountTable.reconDate], row[AccountTable.balance]))
            }
        }
        return account
    }

    fun getAllAccounts(): List<Account> {
        val accounts = ArrayList<Account>()
        transaction {
            addLogger(StdOutSqlLogger)
            AccountTable.selectAll().forEach { row ->
                accounts.add(Account(
                        row[AccountTable.id].value,
                        row[AccountTable.name],
                        CurrencyUnit.of(row[AccountTable.currency]),
                        toBalance(row[AccountTable.currency], row[AccountTable.reconDate], row[AccountTable.balance])
                ))
            }

        }
        return accounts
    }

    fun addTransaction(date: LocalDate, description: String, amount: BigDecimal, fromAccountId: UUID, toAccountId: UUID): String {
        var id: String? = null
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            val entityId = TransactionTable.insert { row ->
                row[TransactionTable.transactionDate] = toDateTime(date)
                row[TransactionTable.description] = description
                row[TransactionTable.amount] = amount
                row[TransactionTable.fromAccount] = EntityID(fromAccountId, AccountTable)
                row[TransactionTable.toAccount] = EntityID(toAccountId, AccountTable)
            } get TransactionTable.id

            id = entityId?.value.toString()

        }
        when (id) {
            null -> throw Exception("Error while trying to insert transaction.")
            else -> return id as String
        }
    }

    fun addTransaction(transaction: Transaction): String {
        var id: String? = null
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            val entityId = TransactionTable.insert { row ->
                row[TransactionTable.transactionDate] = toDateTime(transaction.date)
                row[TransactionTable.description] = transaction.description
                row[TransactionTable.amount] = transaction.amount.amount
                row[TransactionTable.fromAccount] = EntityID(transaction.from.id, AccountTable)
                row[TransactionTable.toAccount] = EntityID(transaction.to.id, AccountTable)
            } get TransactionTable.id

            id = entityId?.value.toString()

        }
        when (id) {
            null -> throw Exception("Error while trying to insert $transaction")
            else -> return id as String
        }
    }

    fun getTransactions(account: Account): List<Transaction> {
        val transactions = ArrayList<Transaction>()
        transaction {
            addLogger(StdOutSqlLogger)

            TransactionTable.select {
                (TransactionTable.fromAccount eq account.id) or (TransactionTable.toAccount eq account.id)
            }.forEach { row ->
                val fromAccount = getAccount(row[TransactionTable.fromAccount].value.toString())!!
                val toAccount = getAccount(row[TransactionTable.toAccount].value.toString())!!
                transactions.add(Transaction(
                        row[TransactionTable.id].value,
                        toLocalDate(row[TransactionTable.transactionDate]),
                        row[TransactionTable.description],
                        toMoney(fromAccount.currency.code, row[TransactionTable.amount]),
                        fromAccount,
                        toAccount))
            }

        }
        return transactions
    }

    fun getAllTransactions(): List<Transaction> {
        val transactions = ArrayList<Transaction>()
        transaction {
            addLogger(StdOutSqlLogger)

            TransactionTable.selectAll().forEach { row ->
                        val fromAccount = getAccount(row[TransactionTable.fromAccount].value.toString())!!
                        val toAccount = getAccount(row[TransactionTable.toAccount].value.toString())!!
                        transactions.add(Transaction(
                                row[TransactionTable.id].value,
                                toLocalDate(row[TransactionTable.transactionDate]),
                                row[TransactionTable.description],
                                toMoney(fromAccount.currency.code, row[TransactionTable.amount]),
                                fromAccount,
                                toAccount))
                    }

        }
        return transactions
    }

    fun getTransactions(fromDate: LocalDate): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun updateTransaction(date: LocalDate, description: String, amount: BigDecimal, fromAccount: UUID, toAccount: UUID): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun deleteTransaction(id: UUID) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

object AccountTable : UUIDTable("tbl_account") {
    val name = text("name")
    val currency = text("currency")
    val reconDate = date("recon_date")
    val balance = decimal("balance", 8, 18)
}

class AccountRow(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AccountRow>(AccountTable)

    var name by AccountTable.name
    var currency by AccountTable.currency
    var reconDate by AccountTable.reconDate
    var balance by AccountTable.balance
}

object TransactionTable : UUIDTable("tbl_transaction") {
    val transactionDate = date("transaction_date")
    val description = text("description")
    val amount = decimal("amount", 8, 18)
    val fromAccount = reference("from_acc_id", AccountTable)
    val toAccount = reference("to_acc_id", AccountTable)
}

class TransactionRow(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TransactionRow>(TransactionTable)

    var transactionDate by TransactionTable.transactionDate
    var description by TransactionTable.description
    var amount by TransactionTable.amount
    val fromAccount by AccountRow referencedOn TransactionTable.fromAccount
    val toAccount by AccountRow referencedOn TransactionTable.toAccount
}

object RecurringTransactionTable : UUIDTable("tbl_transaction") {
    val fromDate = date("from_date")
    val toDate = date("to_date")
    val freqType = text("freq_type")
    val freqCount = text("freq_count")
    val amount = decimal("amount", 8, 18)
    val fromAccount = reference("from_acc_id", AccountTable)
    val toAccount = reference("to_acc_id", AccountTable)
}

class RecurringTransactionRow(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RecurringTransactionRow>(RecurringTransactionTable)

    val fromDate by RecurringTransactionTable.fromDate
    val toDate by RecurringTransactionTable.toDate
    val freqType by RecurringTransactionTable.freqType
    val freqCount by RecurringTransactionTable.freqCount
    val amount by RecurringTransactionTable.fromDate
    val fromAccount by AccountRow referencedOn RecurringTransactionTable.fromAccount
    val toAccount by AccountRow referencedOn RecurringTransactionTable.toAccount
}
