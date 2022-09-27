package com.satyasoft.myschoolavhiyan.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.DataImportAndExportCsvFile
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus


class LoginActivity : AppCompatActivity() {
    private lateinit var userLoginEmail: TextInputEditText
    private lateinit var userLoginPassword: TextInputEditText
    private lateinit var forgotPassword : TextView
    private lateinit var signUp : TextView
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var  preferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private val CSV_FILE_NAME = "Mo School Abhiyan.csv"
    private val STORAGE_PERMISSION_CODE = 100
    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.satyasoft.myschoolavhiyan.R.layout.login_activity)

        ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE), 1)

            if (!Environment.isExternalStorageManager()){
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                startActivity(intent)
            }


        userLoginEmail = findViewById(com.satyasoft.myschoolavhiyan.R.id.userEmail)
        userLoginPassword = findViewById(com.satyasoft.myschoolavhiyan.R.id.userPassword)
        val copyRight = findViewById<TextView>(com.satyasoft.myschoolavhiyan.R.id.copyright)
        copyRight.isSelected = true
         loginButton = findViewById(com.satyasoft.myschoolavhiyan.R.id.login)
         forgotPassword = findViewById(com.satyasoft.myschoolavhiyan.R.id.forgotPassword)
         signUp = findViewById(com.satyasoft.myschoolavhiyan.R.id.signup)
        progressBar = findViewById(com.satyasoft.myschoolavhiyan.R.id.progressBarLarge)


        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        signUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(this@LoginActivity)
                .studentRegistrationDAO().getAllStudentRecord()
            if(getUserId.isEmpty()){
              //  importCSV()
            }

            val getStudentDetails = SchoolMasterDatabase.getSchoolMasterDataBase(this@LoginActivity)
                .studentCollectionRegistrationDAO().getAllStudentCollectionRecord()
            if(getStudentDetails.isEmpty()) {
               //importStudentCollectionCSV()
                DataImportAndExportCsvFile.importStudentCollectionFromCSV(this@LoginActivity)
            }
           userLogin()
        }

    }

    private fun  userLogin() {
        val email = userLoginEmail.text.toString()
        val pass = userLoginPassword.text.toString()

        if(email.isEmpty() || pass.isEmpty()){
            CustomDialogs.commonDialog(this@LoginActivity,getString(com.satyasoft.myschoolavhiyan.R.string.action_login_fail),
                getString(com.satyasoft.myschoolavhiyan.R.string.login_no_empty_field_msg),getString(
                    com.satyasoft.myschoolavhiyan.R.string.dialog_ok_button))
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

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { it ->
            if (it.isSuccessful) {
                progressBar.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userId",email)
                startActivity(intent)
                userLoginEmail.text?.clear()
                userLoginPassword.text?.clear()
                this.getSharedPreferences("Login", Context.MODE_PRIVATE).also { preferences = it }
                editor = preferences.edit()
                editor.putBoolean("isUserLogin", true)
                editor.commit()
            } else {
                progressBar.visibility = View.GONE
                CustomDialogs.commonDialog(
                    this@LoginActivity,
                    getString(com.satyasoft.myschoolavhiyan.R.string.action_login_fail),
                    getString(com.satyasoft.myschoolavhiyan.R.string.login_create_new_account_msg),
                    getString(com.satyasoft.myschoolavhiyan.R.string.dialog_ok_button)
                )
            }
        }
    }
 private fun loginInRoomDataBase(email :String, pass:String) {
     progressBar.visibility = View.VISIBLE
     val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(this@LoginActivity)
         .staffRegistrationDAO()
         .findByUserID(
             email,
             pass
         )
     run {

         if (getUserId != null) {
             if (email == getUserId.emailId && pass == getUserId.password) {
                 progressBar.visibility = View.GONE
                 val intent = Intent(this@LoginActivity, MainActivity::class.java)
                 intent.putExtra("userId", email)
                 startActivity(intent)
                 userLoginEmail.text?.clear()
                 userLoginPassword.text?.clear()
                 this.getSharedPreferences("Login", Context.MODE_PRIVATE).also { preferences = it }
                 editor = preferences.edit()
                 editor.putBoolean("isUserLogin", true)
                 editor.commit()
             }
         } else {
             progressBar.visibility = View.GONE
             CustomDialogs.commonDialog(
                 this@LoginActivity,
                 getString(com.satyasoft.myschoolavhiyan.R.string.action_login_fail),
                 getString(com.satyasoft.myschoolavhiyan.R.string.login_create_new_account_msg),
                 getString(com.satyasoft.myschoolavhiyan.R.string.dialog_ok_button)
             )
         }
     }
 }

}