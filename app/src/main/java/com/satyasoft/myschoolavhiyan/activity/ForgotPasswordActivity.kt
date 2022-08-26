package com.satyasoft.myschoolavhiyan.activity

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.View
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs

class ForgotPasswordActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var resetEmail : TextInputEditText
    private lateinit var resetButton : Button
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        resetEmail = findViewById(R.id.resetPassword)
        resetButton = findViewById(R.id.resetButton)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBarLarge)

        resetButton.setOnClickListener {
            var email = resetEmail.text.toString().trim()
//            if (EmailValidator.isEmailValid(resetEmail.text.toString())){
//                email = resetEmail.text.toString()
//            }else{
//                Toast.makeText(applicationContext, getString(R.string.valid_mail_id), Toast.LENGTH_SHORT).show()
//            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = android.view.View.VISIBLE
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressBar.visibility = android.view.View.GONE
                            finish()
                          //  Toast.makeText(this@ForgotPasswordActivity, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                        } else {
                            CustomDialogs.commonDialog(
                                this@ForgotPasswordActivity,
                                getString(R.string.forgot_password),
                                getString(R.string.unable_to_reset),
                                getString(R.string.dialog_ok_button)
                            )
                          //  Toast.makeText(this@ForgotPasswordActivity, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

}