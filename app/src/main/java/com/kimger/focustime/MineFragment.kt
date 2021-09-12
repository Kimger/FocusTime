package com.kimger.focustime

import android.widget.Toast
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.permission.PermissionUtils
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 15:22
 * @email kimger@cloocle.com
 * @description
 */
class MineFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_mine

    override fun init() {
        val lockMode = Hawk.get("lockMode", 0)
        sw_lock.isChecked = lockMode != 0
        sw_lock.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                // 系统浮窗需要先进行权限审核，有权限则创建app浮窗
                if (PermissionUtils.checkPermission(mActivity)) setLockMode()
                // 申请浮窗权限
                else requestPermission()
            } else {
                closeLockMode()
            }
        }
    }

    private fun setLockMode() {
        Hawk.put("lockMode", 1)
    }

    private fun closeLockMode() {
        Hawk.put("lockMode", 0)
    }

    private fun requestPermission() =
        PermissionUtils.requestPermission(mActivity, object : OnPermissionResult {
            override fun permissionResult(isOpen: Boolean) {
                if (isOpen) {
                    sw_lock.isChecked = true
                    setLockMode()
                } else {
                    sw_lock.isChecked = false
                    closeLockMode()
                    Toast.makeText(mActivity, "请先打开悬浮窗权限", Toast.LENGTH_SHORT).show()
                }
            }
        })
}