package com.satyasoft.myschoolavhiyan.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.database.StaffRegistration
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.EmailValidator
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus


class SignupActivity : AppCompatActivity() {
    private lateinit var userEmail: TextInputEditText
    private lateinit var userConfirmPassword: TextInputEditText
    private lateinit var userPassword: TextInputEditText
    private lateinit var signUpButton: Button

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userEmail = findViewById(R.id.userName)
        userConfirmPassword = findViewById(R.id.password)
        userPassword = findViewById(R.id.confirmPassword)
        signUpButton = findViewById(R.id.signupMe)
        progressBar = findViewById(R.id.progressBarLarge)
        // Initialising auth object
        auth = Firebase.auth

        // userEmail.setText()
        signUpButton.setOnClickListener {
            signUpUser()
        }

    }

    private fun signUpUser() {

        var email = userEmail.text.toString()
        val pass = userPassword.text.toString()
        val confirmPassword = userConfirmPassword.text.toString()

        if (EmailValidator.isEmailValid(userEmail.text.toString())) {
            email = userEmail.text.toString()
        } else {
            Toast.makeText(applicationContext,
                getString(R.string.valid_mail_id),
                Toast.LENGTH_SHORT).show()
        }

        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
      //  progressBar.visibility = View.VISIBLE

        if (NetworkConnectionStatus.checkConnection(this@SignupActivity)) {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                if (it.isSuccessful) {

                    CustomDialogs.commonDialog(
                        this@SignupActivity,
                        getString(R.string.signup_me),
                        getString(R.string.data_saved),
                        getString(R.string.dialog_ok_button)
                    )
                    userEmail.text?.clear()
                    userPassword.text?.clear()
                    finish()
                } else {
                    CustomDialogs.commonDialog(
                        this@SignupActivity,
                        getString(R.string.signup_fail),
                        getString(R.string.signup_not_done),
                        getString(R.string.dialog_ok_button)
                    )
                    Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            SchoolMasterDatabase.getSchoolMasterDataBase(this@SignupActivity)
                .staffRegistrationDAO()
                .insertAll(
                    StaffRegistration(
                        0, email, pass
                    ),
                )
            CustomDialogs.commonDialog(
                this@SignupActivity,
                getString(R.string.signup_me),
                getString(R.string.data_saved),
                getString(R.string.dialog_ok_button)
            )
            userEmail.text?.clear()
            userPassword.text?.clear()
            finish()
          //  progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

}