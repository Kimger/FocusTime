package com.kimger.focustime

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import kotlinx.coroutines.*
import pub.devrel.easypermissions.EasyPermissions
import kotlin.coroutines.CoroutineContext

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/22 10:05
 * @description
 */
abstract class BaseActivity : AppCompatActivity(), IActivity, CoroutineScope {

    protected val TAG: String = javaClass.simpleName

    private var loadingDialog: Dialog? = null
    private var isAutoRefresh = false

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**
     * onCreate统一进行View相关初始化操作，不允许重写
     */
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled()
        beforeCreate()
        if (getLayoutId() == 0) return
        setContentView(getLayoutId())
        job = Job()
        ImmersionBar.with(this).init()
        onCreated(savedInstanceState)
        init()
        val view_status = findViewById<View>(R.id.view_status)
        if (view_status != null) {
            setStatusBar(view_status)
        }
        initEvent()
        initData()
        onInitialized(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun beforeCreate() {
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    /**
     * Activity创建成功调用此方法，替代onCreate
     */
    override fun onCreated(saveInstanceState: Bundle?) {

    }

    /**
     * 初始化完毕
     */
    override fun onInitialized(saveInstanceState: Bundle?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun startActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }

    override fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        startActivityForResult(Intent(this, cls), requestCode)
    }

    override fun startActivity(key: String, value: String, cls: Class<*>) {
        val intent = Intent(this, cls)
        intent.putExtra(key, value)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    fun getText(editText: EditText) = editText.text.toString().trim()

    fun getText(textView: TextView) = textView.text.toString().trim()

    open fun setStatusBarDarkFont() = true

    open fun keyboardEnable() = false

//    protected fun setStatusBarDarkFont(b: Boolean) {
//        val brand = RomHelper.getBrand()
//        val androidVersionInt = RomHelper.getAndroidVersionInt()
//        if ("vivo".equals(brand, ignoreCase = true)) {
//            if (androidVersionInt < 600) {
//                ImmersionBar.with(this).statusBarDarkFont(b).statusBarAlpha(0.3f).init()
//            } else {
//                ImmersionBar.with(this).statusBarDarkFont(b).init()
//            }
//        } else {
//            ImmersionBar.with(this).statusBarDarkFont(b).init()
//        }
//        ImmersionBar.with(this).keyboardEnable(keyboardEnable()).init()
//    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard(editText: EditText) {
        if (Helper.isNotNull(editText) && Helper.isNotNull(editText.windowToken)) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    editText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        }
    }

    fun showKeyboard(editText: EditText) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(editText, 0)

    }


    /**
     * 显示加载对话框
     */
    fun startLoading() {
        GlobalScope.launch(Dispatchers.Main) {
            if (isFinishing) {
                return@launch
            }
            if (loadingDialog != null) {
                if (!loadingDialog!!.isShowing) {
                    loadingDialog!!.show()
                }
                return@launch
            }
            loadingDialog = LoadingDialog(this@BaseActivity)
            try {
                loadingDialog?.show()
            } catch (e: Exception) {
            }

        }
    }

    /**
     * 隐藏加载对话框
     */
    fun endLoading() {
        GlobalScope.launch(Dispatchers.Main) {
            if (loadingDialog != null && loadingDialog!!.isShowing && !isFinishing) {
                loadingDialog!!.dismiss()
                loadingDialog = null
            }
        }
    }

    private fun setStatusBar(view_status: View) {
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val statusBarHeight = this.resources.getDimensionPixelSize(resourceId)
            val layoutParams = view_status.layoutParams
            layoutParams.height = statusBarHeight
            view_status.layoutParams = layoutParams
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!allowBackKeyDown()) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    open fun allowBackKeyDown(): Boolean {
        return true
    }
}