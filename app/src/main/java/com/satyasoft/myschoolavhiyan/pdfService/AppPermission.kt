package com.satyasoft.myschoolavhiyan.pdfService

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.satyasoft.myschoolavhiyan.activity.ui.studentDetails.StudentDetailInfoFragment


class AppPermission {
    companion object {
        const val REQUEST_PERMISSION: Int = 123
        fun permissionGranted(context: Context) =
            ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED

        fun requestPermission(activity: FragmentActivity?) {
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION
                )
            }
        }
    }
}