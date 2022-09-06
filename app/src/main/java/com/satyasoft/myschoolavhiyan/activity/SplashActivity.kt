package com.satyasoft.myschoolavhiyan.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.satyasoft.myschoolavhiyan.R


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        splashScreen.setKeepOnScreenCondition { true }

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("Login",Context.MODE_PRIVATE)
        if (sharedPreferences.contains("isUserLogin")) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java);
            startActivity(intent);
        } else {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}