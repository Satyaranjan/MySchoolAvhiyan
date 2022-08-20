package com.satyasoft.myschoolavhiyan.utils

sealed  class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    data class Failure(
        val message: String?,
        val s: String
    ): ResultOf<Nothing>()


}