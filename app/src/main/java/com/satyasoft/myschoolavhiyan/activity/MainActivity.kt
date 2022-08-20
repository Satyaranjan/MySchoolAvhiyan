package com.satyasoft.myschoolavhiyan.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var  auth: FirebaseAuth? = null
    companion object {
          lateinit var userId :String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        userId = intent.getStringExtra("userId").toString()
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        auth = FirebaseAuth.getInstance()
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val headerView: View = navView.getHeaderView(0)
        val imageView : ImageView? = headerView.findViewById(R.id.emailImageView)
        val userName :TextView? = headerView.findViewById(R.id.userNameInEmail)
        val emailId :TextView? = headerView.findViewById(R.id.emailIdUser)

        val user = Firebase.auth.currentUser

            user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            val parts = email?.split(".")?.toTypedArray()
            val mFirebaseUser = auth!!.currentUser
            if (emailId != null && user != null) {
                emailId.text = email
            }
            if (mFirebaseUser != null) {
                userName?.text  = parts?.get(0).toString().toUpperCase(Locale.getDefault())
            }
            if (mFirebaseUser?.photoUrl != null) {
                imageView?.setImageURI(photoUrl)
            }
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerIds = profile.providerId

                // UID specific to the provider
                val uids = profile.uid

                // Name, email address, and profile photo Url
                val names = profile.displayName
                val emails = profile.email
                val photoUrls = profile.photoUrl
                Log.d("User","User Details ------1---->>$names")
                Log.d("User","User Details ------2---->>$emails")
                Log.d("User","User Details -------3--->>$photoUrls")

            }
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.nav_sltudent_details ,R.id.nav_registration, R.id.nav_gallery
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}