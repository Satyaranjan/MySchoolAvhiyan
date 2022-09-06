package com.satyasoft.myschoolavhiyan.pdfService

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity


class AppPermission {
    companion object {
        const val REQUEST_PERMISSION: Int = 123
        fun permissionGranted(context: Context) =
            ActivityCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED

        fun requestPermission(activity: FragmentActivity?) {
            if (activity != null) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                            WRITE_EXTERNAL_STORAGE,
                            READ_EXTERNAL_STORAGE),
                        REQUEST_PERMISSION
                    )
                }else{
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            WRITE_EXTERNAL_STORAGE,
                            READ_EXTERNAL_STORAGE),
                        REQUEST_PERMISSION
                    )
                }
            }
        }
    }
}