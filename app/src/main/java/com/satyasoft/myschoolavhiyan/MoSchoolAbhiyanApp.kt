package com.satyasoft.myschoolavhiyan

import android.app.Application


class MoSchoolAbhiyanApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MoSchoolAbhiyanApp? = null
        fun applicationContext() : MoSchoolAbhiyanApp {
            return instance as MoSchoolAbhiyanApp
        }
    }

    override fun onCreate() {
        super.onCreate()

    }
}