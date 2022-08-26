package com.satyasoft.myschoolavhiyan.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus


class LoginActivity : AppCompatActivity() {
    private lateinit var userLoginEmail: TextInputEditText
    private lateinit var userLoginPassword: TextInputEditText
    private lateinit var forgotPassword : TextView
    private lateinit var signUp : TextView
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        userLoginEmail = findViewById(R.id.userEmail)
        userLoginPassword = findViewById(R.id.userPassword)
        val copyRight = findViewById<TextView>(R.id.copyright)
        copyRight.isSelected = true
         loginButton = findViewById(R.id.login)
         forgotPassword = findViewById(R.id.forgotPassword)
         signUp = findViewById(R.id.signup)
        progressBar = findViewById(R.id.progressBarLarge)
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        signUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            userLogin()
        }
    }

    private fun  userLogin() {
        val email = userLoginEmail.text.toString()
        val pass = userLoginPassword.text.toString()

        if(email.isEmpty() || pass.isEmpty()){
            CustomDialogs.commonDialog(this@LoginActivity,getString(R.string.action_login_fail),
                getString(R.string.login_no_empty_field_msg),getString(R.string.dialog_ok_button))
        }else {
            if(NetworkConnectionStatus.checkConnection(this@LoginActivity)) {
                loginInFireBase(email,pass)
            }else{
                loginInRoomDataBase(email,pass)
            }
        }
    }

    private fun loginInFireBase(email :String, pass:String){
        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                progressBar.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userId",email)
                startActivity(intent)
                userLoginEmail.text?.clear()
                userLoginPassword.text?.clear()
            } else {
                CustomDialogs.commonDialog(
                    this@LoginActivity,
                    getString(R.string.action_login_fail),
                    getString(R.string.login_create_new_account_msg),
                    getString(R.string.dialog_ok_button)
                )
            }
        }
    }
 private fun loginInRoomDataBase(email :String, pass:String){
     run {

         val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(this@LoginActivity)
             .staffRegistrationDAO()
             .findByUserID(
                 email,
                 pass
             )
         if (getUserId != null) {
             if (email == getUserId.emailId && pass == getUserId.password) {
                 val intent = Intent(this@LoginActivity, MainActivity::class.java)
                 intent.putExtra("userId",email)
                 startActivity(intent)
                 userLoginEmail.text?.clear()
                 userLoginPassword.text?.clear()
             }
         } else{
             CustomDialogs.commonDialog(
                 this@LoginActivity,
                 getString(R.string.action_login_fail),
                 getString(R.string.login_create_new_account_msg),
                 getString(R.string.dialog_ok_button)
             )
         }
     }

     // Toast.makeText(applicationContext,"NoInternet Connection !!!",Toast.LENGTH_SHORT).show()

 }

}