package com.kimger.focustime

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kimger.focustime.sql.DatabaseManager
import com.kimger.focustime.sql.entity.TodoListEntity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun getLayoutId() = R.layout.activity_main

    override fun init() {
        handleDefaultData()
        bnv_home.setOnNavigationItemSelectedListener(this)
        val fragments: ArrayList<Fragment> = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(MineFragment())
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments)
        vp_home.adapter = pagerAdapter

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.home -> {
                vp_home.currentItem = 0
            }
            R.id.mine -> {
                vp_home.currentItem = 1
            }
        }
        return true
    }

    private fun handleDefaultData(){
        if (isFirstRun()){
            Hawk.put("isFirstRun",false)
            createDefaultTodo()
        }
    }
    private fun createDefaultTodo(){
        val title = "点击右上角添加代办"
        val time = 100000L
        val todoEntity = TodoListEntity(title, time)
        DatabaseManager.dataBase.todoDao().insertTodo(todoEntity)
    }

    private fun isFirstRun():Boolean{
        return Hawk.get("isFirstRun",true)
    }
}