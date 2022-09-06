package com.satyasoft.myschoolavhiyan.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
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
import com.satyasoft.myschoolavhiyan.activity.ui.studentGallery.GlideApp.with
import com.satyasoft.myschoolavhiyan.databinding.ActivityMainBinding
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus
import com.squareup.picasso.Picasso
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var  auth: FirebaseAuth? = null
    private var currentUser : String? = null
    companion object {
          lateinit var userId :String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        userId = intent.getStringExtra("userId").toString()

        currentUser = intent.getStringExtra("userId")
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
       // val myPics = FirebaseAuth.getInstance().currentUser!!.photoUrl
       // imageView?.setImageURI(myPics)
        if(NetworkConnectionStatus.checkConnection(this@MainActivity)) {
            val user = Firebase.auth.currentUser

            user?.let {
                // Name, email address, and profile photo Url
                val name = user.displayName
                val email = user.email
               val photoUrl = auth!!.currentUser?.photoUrl?.toString()
                Picasso.get().load(user.photoUrl)
                    .into(imageView);

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
                    userName?.text = parts?.get(0).toString().toUpperCase(Locale.getDefault())
                }
                if (mFirebaseUser?.photoUrl != null) {
                   // imageView?.setImageURI(photoUrl)
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

                }
            }
        }else{
            val parts = currentUser?.split(".")?.toTypedArray()
            emailId?.text = currentUser
            userName?.text = parts?.get(0).toString().toUpperCase(Locale.getDefault())
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.nav_sltudent_details ,R.id.nav_registration, R.id.nav_gallery , R.id.nav_graph
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menu is MenuBuilder) (menu as MenuBuilder).setOptionalIconsVisible(true)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.action_signOut -> {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            val preferences : SharedPreferences =  this.getSharedPreferences("Login", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = preferences.edit()
            editor.remove("isUserLogin")
            editor.commit()
            finish()
             true
        }
      R.id.action_upload ->{
          val intent = Intent(this@MainActivity, UploadImagesIntoServer::class.java)
          startActivity(intent)
          finish()
          true
      }
        else -> super.onOptionsItemSelected(item)
    }

}