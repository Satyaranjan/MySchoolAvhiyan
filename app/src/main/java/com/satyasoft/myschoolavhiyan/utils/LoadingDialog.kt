package com.satyasoft.myschoolavhiyan.utils

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import com.satyasoft.myschoolavhiyan.R

class LoadingDialog(private val activity : Activity) {

    private lateinit var dialog: AlertDialog

    val TAG = "Dialog"

    fun startLoadingDialog(){
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_progres_bar, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
        Log.i(TAG,"dialog show")
    }

    fun dismissDialog(){
        dialog.dismiss()
        Log.i(TAG,"dialog dismiss")
    }

}