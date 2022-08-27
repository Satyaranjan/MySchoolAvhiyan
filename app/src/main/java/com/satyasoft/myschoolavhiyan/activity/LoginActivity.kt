package com.satyasoft.myschoolavhiyan.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.opencsv.CSVReader
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus
import java.io.File
import java.io.FileReader
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private lateinit var userLoginEmail: TextInputEditText
    private lateinit var userLoginPassword: TextInputEditText
    private lateinit var forgotPassword : TextView
    private lateinit var signUp : TextView
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var studentInfoLists : MutableList<StudentDetails>
    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.satyasoft.myschoolavhiyan.R.layout.login_activity)

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
           userLogin()
           readPdfData()
            importCSV()
        }
        val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(this@LoginActivity)
            .studentRegistrationDAO().getAllStudentRecord()
        if(getUserId.isEmpty()){
          //  importCSV()
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
                    getString(com.satyasoft.myschoolavhiyan.R.string.action_login_fail),
                    getString(com.satyasoft.myschoolavhiyan.R.string.login_create_new_account_msg),
                    getString(com.satyasoft.myschoolavhiyan.R.string.dialog_ok_button)
                )
            }
        }
    }
 private fun loginInRoomDataBase(email :String, pass:String) {
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
                 intent.putExtra("userId", email)
                 startActivity(intent)
                 userLoginEmail.text?.clear()
                 userLoginPassword.text?.clear()
             }
         } else {
             CustomDialogs.commonDialog(
                 this@LoginActivity,
                 getString(com.satyasoft.myschoolavhiyan.R.string.action_login_fail),
                 getString(com.satyasoft.myschoolavhiyan.R.string.login_create_new_account_msg),
                 getString(com.satyasoft.myschoolavhiyan.R.string.dialog_ok_button)
             )
         }
     }
 }
 private fun readPdfData(){
     val reader: PdfReader
     var extractedText = ""
     val myData : ArrayList<String> = ArrayList()
     try {
         val file =
             File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                 .toString() + "/" + "MoSchoolAbhiyan.pdf")
         reader = PdfReader(file.toString())

          val n = reader.numberOfPages
         for (i in 0 until n) {
             """$extractedText${PdfTextExtractor.getTextFromPage(reader, i + 1).trim { it <= ' ' }} """.trimIndent()
                 .also { extractedText = it }
         }
         // pageNumber = 1
        // val textFromPage = PdfTextExtractor.getTextFromPage(reader, 2)
         myData.add(extractedText)
         val parts = extractedText?.split(" ")?.toTypedArray()
         val ssdd =  parts?.get(20).toString()
         println("print ----$ssdd")
         println(extractedText)
         reader.close()


     } catch (e: IOException) {
         e.printStackTrace()
     }
 }
    private fun importCSV(){

        try {
            val file =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/" + "/CSV/MoSchoolAbhiyan.csv")
            if(file.exists()) {
                val fileReader = FileReader(file)
                val reader = CSVReader(fileReader)
                 reader.skip(3)
                var record: Array<String>? = null
                var mId: String
                var mName: String
                var mEmail: String
                var mPhoneNo: String
                var mYearOfPass: String
                var mAmount: String
                while (reader.readNext().also { record = it } != null) {
                    mId = record!![0]
                    mName = record!![1]
                    mEmail = record!![2]
                    mPhoneNo = record!![3]
                    mYearOfPass = record!![4]
                    mAmount = record!![5]
                    this@LoginActivity?.let { it1 ->
                        SchoolMasterDatabase.getSchoolMasterDataBase(it1)
                            .studentRegistrationDAO().insertAllStudentRecord(
                                StudentDetails(
                                    mId.toInt(), mName, mEmail, mPhoneNo, mYearOfPass, mAmount
                            )
                       )
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}