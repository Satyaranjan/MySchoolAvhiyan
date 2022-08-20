package com.satyasoft.myschoolavhiyan.utils

import com.satyasoft.myschoolavhiyan.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface


class CustomDialogs {

    companion object {
        fun commonDialog(context: Context?, title: String?, message: String?, buttonName: String?) {
            val builder = AlertDialog.Builder(context)
            if (context != null) {
                with(builder) {
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton(buttonName, null)
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()

            val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            with(button) {
                setBackgroundColor(Color.WHITE)
                setBackgroundResource(R.drawable.round_conner_outline)
                setPadding(0, 0, 20, 0)
                setTextColor(Color.BLACK)
                typeface = Typeface.DEFAULT_BOLD
                alertDialog.closeOptionsMenu()
            }
        }



    }

}