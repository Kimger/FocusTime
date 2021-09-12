package com.kimger.focustime

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.ProgressBar

class LoadingDialog(context: Context) : Dialog(context, R.style.progress_dialog) {


    init {
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        val progressBar = findViewById<ProgressBar>(R.id.pb_loading)
    }

    override fun onStart() {
        super.onStart()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.CENTER)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isShowing) {
            dismiss()
        }
    }

}