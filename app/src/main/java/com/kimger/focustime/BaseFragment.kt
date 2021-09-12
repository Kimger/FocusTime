package com.kimger.focustime

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/22 14:34
 * @description
 */
abstract class BaseFragment: Fragment(), IFragment , CoroutineScope {
    protected val TAG = "${javaClass.simpleName}(${javaClass.hashCode()})"

    protected var isAttach = false
    protected var isViewCreated = false
    protected var isVisibleToUser = false
    private var isDataLoaded = false
    private var isAutoRefresh = false
    private var loadingDialog: Dialog? = null

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    protected lateinit var mActivity: BaseActivity

    protected lateinit var rootView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
        isAttach = true
    }

    override fun onDetach() {
        super.onDetach()
        isAttach = false
    }

    /**
     * 进行初始化操作，不允许重写
     */
    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        beferCreated()
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    /**
     * 进行初始化操作，不允许重写
     */
    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initViewModel()
        job = Job()
        init()
        isViewCreated = true
        val view_status = rootView.findViewById<View>(R.id.view_status)
        if (view_status != null) {
            setStatusBar(view_status)
        }
        initData()
        onInitialized()
        tryToLoadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        isViewCreated = false
        isDataLoaded = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        isDataLoaded = false
    }

    /**
     * 初始化之前
     */
    override fun beferCreated() {

    }

    /**
     * 完全初始化以后
     */
    override fun onInitialized() {

    }

    override fun initEvent() {

    }

    override fun initData() {
    }

    override fun loadData() {

    }

    /**
     * 懒加载
     */
    override fun lazyLoad() {
        Log.d(TAG, "lazyLoad: ")
//        initData()
        loadData()
        initEvent()
    }

    /**
     * 进行懒加载尝试
     */
    private fun tryToLoadData() {
        if (isViewCreated and isVisibleToUser and !isDataLoaded) {
            lazyLoad()
            isDataLoaded = true
            dispatchParentVisibleState()
        }
    }

    /**
     * ViewPager嵌套下，判断父fragment是否可见
     */
    private fun isParentVisible(): Boolean {
        val fragment = parentFragment
        return fragment == null || (fragment is BaseFragment && fragment.isVisibleToUser)
    }

    /**
     * ViewPager，如果当前fragment可见，并且其子fragment也可见，尝试让子fragment加载数据
     */
    private fun dispatchParentVisibleState() {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        fragments.ifEmpty { return }
        for (child in fragments) {
            if (child is BaseFragment && child.isVisibleToUser) {
                child.tryToLoadData()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryToLoadData()
        if (isVisibleToUser && isAttach) {
            setStatusBar(isVisibleToUser)
        }
    }

    /**                                                                                      
     * 重写此方法进行状态栏设置
     */
    override fun setStatusBar(visible: Boolean) {}


    override fun startActivity(cls: Class<*>) {
        startActivity(Intent(mActivity, cls))
    }


    override fun startActivity(key: String, value: String, cls: Class<*>) {
        val intent = Intent(mActivity, cls)
        intent.putExtra(key, value)
        startActivity(intent)
    }

    /**
     * 获取arguments存储的数据
     * @param name 存入的数据key
     */
    fun <T> getArgument(name: String): T {
        return arguments?.get(name) as T
    }

    /**
     * 显示加载对话框
     */
    fun startLoading() {
        GlobalScope.launch(Dispatchers.Main) {
            if (!isAttach) {
                return@launch
            }
            if (loadingDialog != null) {
                if (!loadingDialog!!.isShowing) {
                    loadingDialog!!.show()
                }
                return@launch
            }
            loadingDialog = LoadingDialog(mActivity)
            loadingDialog?.show()
        }
    }

    /**
     * 隐藏加载对话框
     */
    fun endLoading() {
        GlobalScope.launch(Dispatchers.Main) {
            if (loadingDialog != null && loadingDialog!!.isShowing && isAttach) {
                loadingDialog!!.dismiss()
                loadingDialog = null
            }
        }
    }

    fun setStatusBar(view_status: View) {
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val statusBarHeight = this.resources.getDimensionPixelSize(resourceId)
            val layoutParams = view_status.layoutParams
            layoutParams.height = statusBarHeight
            view_status.layoutParams = layoutParams
        }
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard(editText: EditText) {
        if (Helper.isNotNull(editText) && Helper.isNotNull(editText.windowToken)) {
            (mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    editText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        }
    }

    fun showKeyboard(editText: EditText) {
        (mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(editText, 0)

    }
}

