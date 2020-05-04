@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.gm_demo.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.gm_demo.R
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    /**
     * Common progress dialog
     */
    @SuppressLint("InflateParams")
    @JvmStatic
    fun progressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_progress, null)
        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun materialDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun formateDateString(
        inputDateTimeFormate: String,
        outputDateTimeFormat: String,
        date: String
    ): String {
        val inputDateFormate = SimpleDateFormat(inputDateTimeFormate, Locale.ENGLISH)
        val outputDateFormate = SimpleDateFormat(outputDateTimeFormat, Locale.ENGLISH)
        val date1 = inputDateFormate.parse(date)
        return outputDateFormate.format(date1)
    }



    /**
     * Remove [] from Error Objects when there are multiple errors
     *
     * @param message as String
     * @return replacedString
     */
    fun removeArrayBrace(message: String): String {
        return message.replace("[\"", "").replace("\"]", "").replace(".", "")
    }


}