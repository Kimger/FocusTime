package com.kimger.focustime

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/22 14:27
 * @description
 */
class ViewPagerAdapter(fragmentManager: FragmentManager, private var fragments: List<Fragment>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

}