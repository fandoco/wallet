package com.fandoco.wallet

import org.joda.money.CurrencyUnit
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@CrossOrigin(origins = ["*"])
class SecureDataController {

    @GetMapping("/accounts")
    fun getAccounts(): List<Account> {
        return TransactionRepository.getAccounts()
    }

    @PostMapping("/accounts")
    fun addAccount(@RequestBody body: Map<String, String>): String {
        return TransactionRepository.addAccount(
                body["name"]!!,
                CurrencyUnit.of(body["currency"])!!,
                LocalDate.parse(body["reconDate"]!!),
                BigDecimal(body["balance"]!!)
        )
    }

    @PutMapping("/accounts")
    fun updateAccount(@RequestBody body: Map<String, String>): String {
        return TransactionRepository.updateAccount(
                body["name"]!!,
                CurrencyUnit.of(body["currency"])!!,
                LocalDate.parse(body["reconDate"]!!),
                BigDecimal(body["balance"]!!))
    }

    @DeleteMapping("/accounts")
    fun deleteAccount(@RequestBody body: Map<String, String>) {
        TransactionRepository.deleteAccount(UUID.fromString(body["id"]!!))
    }

    @GetMapping("/transactions")
    fun getTransactions(@RequestParam(value = "fromDate") fromDate: String): List<Transaction> {
        return TransactionRepository.getTransactions(LocalDate.parse(fromDate))
    }

    @PostMapping("/transactions")
    fun addTransaction(@RequestBody body: Map<String, String>): String {
        return TransactionRepository.addTransaction(
                LocalDate.parse(body["date"]!!),
                body["description"]!!,
                BigDecimal(body["amount"]!!),
                UUID.fromString(body["fromAccountId"]!!),
                UUID.fromString(body["toAccountId"]!!)
        )
    }

    @PutMapping("/transactions")
    fun updateTransaction(@RequestBody body: Map<String, String>): String {
        return TransactionRepository.updateTransaction(
                LocalDate.parse(body["date"]!!),
                body["description"]!!,
                BigDecimal(body["amount"]!!),
                UUID.fromString(body["fromAccountId"]!!),
                UUID.fromString(body["toAccountId"]!!))
    }

    @DeleteMapping("/transactions")
    fun deleteTransaction(@RequestBody body: Map<String, String>) {
        TransactionRepository.deleteTransaction(UUID.fromString(body["id"]!!))
    }

    @RequestMapping("/logout", method = [RequestMethod.POST])
    fun logoutPage(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/"
    }

}