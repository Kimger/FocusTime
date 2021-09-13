package com.kimger.focustime

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/5/18 14:41
 * @description
 */
abstract class BaseDialog : DialogFragment() {

    protected lateinit var rootView: View
    private var onDismissListener: OnDismissListener? = null
    private var loadingDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rootView = inflater.inflate(getLayoutId(), container, false)
        dialog?.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    override fun onStart() {
        super.onStart()
        setDialog()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun init(view: View)

    open fun setDialog() {
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 动画设置
        dialog?.window!!.setGravity(Gravity.CENTER)
        // 宽度、位置设置
//        val width = ScreenUtils.getScreenWidth(activity)
//        val params = dialog?.window!!.attributes
//        params.width = 300
//        dialog?.window!!.attributes = params
//        dialog?.window!!.setWindowAnimations(R.style.dialog_anim_bottom)
//        dialog?.setCanceledOnTouchOutside(false)
    }

    open fun show(manager: FragmentManager) {
        show(manager, javaClass.simpleName)
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (onDismissListener != null) {
            onDismissListener!!.dismiss()
        }
    }

    interface OnDismissListener {
        fun dismiss()
    }

    /**
     * 显示加载对话框
     */
    fun startLoading() {
        MainScope().launch(Dispatchers.Main) {
            if (loadingDialog != null) {
                if (!loadingDialog!!.isShowing) {
                    loadingDialog!!.show()
                }
                return@launch
            }
            loadingDialog = LoadingDialog(rootView?.context)
            loadingDialog?.show()
        }
    }

    /**
     * 隐藏加载对话框
     */
    fun endLoading() {
        MainScope().launch(Dispatchers.Main) {
            if (loadingDialog != null && loadingDialog!!.isShowing) {
                loadingDialog!!.dismiss()
                loadingDialog = null
            }
        }
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard(editText: EditText) {
        if (Helper.isNotNull(editText) && Helper.isNotNull(editText.windowToken)) {
            (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    editText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        }
    }

    fun showKeyboard(editText: EditText) {
        (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(editText, 0)

    }

}