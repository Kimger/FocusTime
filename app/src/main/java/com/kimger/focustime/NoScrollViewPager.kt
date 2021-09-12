package com.kimger.focustime

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/22 15:42
 * @description
 */
class NoScrollViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var noScroll: Boolean = true

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return !noScroll && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return !noScroll && super.onInterceptTouchEvent(ev)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, false)
    }


}