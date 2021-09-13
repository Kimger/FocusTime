package com.kimger.focustime

import android.view.View
import kotlinx.android.synthetic.main.dialog_exit_doing.*

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-13 11:43
 * @email kimger@cloocle.com
 * @description
 */
class ExitDoingDialog : BaseDialog() {

    private lateinit var onPositiveListener: () -> Unit
    private lateinit var onNegativeListener: () -> Unit

    override fun getLayoutId() = R.layout.dialog_exit_doing

    override fun init(view: View) {
        tv_positive.setOnClickListener {
            dismiss()
            onPositiveListener() }
        tv_negative.setOnClickListener {
            onNegativeListener() }
    }

    fun setOnPositiveClick(listener: () -> Unit) {
        onPositiveListener = listener
    }

    fun setOnNegativeClick(listener: () -> Unit) {
        onNegativeListener = listener
    }
}
