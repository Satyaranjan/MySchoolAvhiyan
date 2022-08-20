package com.satyasoft.myschoolavhiyan.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context


/**
 * This class uses the AccountManager to get the primary email address of the
 * current user.
 */
object UserEmailFetcher {

    fun getEmail(context: Context?): String? {
        val accountManager = AccountManager.get(context)
        val account = getAccount(accountManager)
        return account?.name
    }

    private fun getAccount(accountManager: AccountManager): Account? {
        val accounts = accountManager.getAccountsByType("com.google")
        val account: Account? = if (accounts.isNotEmpty()) {
            accounts[0]
        } else {
            null
        }
        return account
    }
}