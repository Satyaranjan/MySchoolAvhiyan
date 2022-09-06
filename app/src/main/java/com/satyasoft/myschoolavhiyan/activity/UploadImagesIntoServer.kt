package com.satyasoft.myschoolavhiyan.activity


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satyasoft.myschoolavhiyan.R
import java.io.IOException
import java.util.*


class UploadImagesIntoServer : AppCompatActivity() {

    private var btnSelect: AppCompatButton? = null
    private var btnUpload: AppCompatButton? = null
    private var imageView: ImageView? = null
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 22
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_images_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSelect = findViewById(R.id.btnChoose)
        btnUpload = findViewById(R.id.btnUpload)
        imageView = findViewById(R.id.imgView)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        btnSelect!!.setOnClickListener(View.OnClickListener { SelectImage() })

        btnUpload!!.setOnClickListener(View.OnClickListener { uploadImage() })
    }

    private fun SelectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."),
            PICK_IMAGE_REQUEST)
    }

    // Override onActivityResult method
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode,
            resultCode,
            data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    // UploadImage method
    private fun uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref = storageReference
                ?.child("images/"
                        + UUID.randomUUID().toString())

            ref!!.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast
                        .makeText(this@UploadImagesIntoServer,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast
                        .makeText(this@UploadImagesIntoServer,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0
                            * taskSnapshot.bytesTransferred
                            / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(
                        "Uploaded "
                                + progress.toInt() + "%")
                }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this@UploadImagesIntoServer, MainActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

}
