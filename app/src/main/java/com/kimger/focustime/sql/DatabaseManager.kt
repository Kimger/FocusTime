package com.kimger.focustime.sql

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kimger.focustime.App

object DatabaseManager {
    private const val DB_NAME = "todoList.db"
    val dataBase:AppDataBase by lazy {
        Room.databaseBuilder(App.instance.applicationContext,AppDataBase::class.java, DB_NAME)
            .addCallback(CreateCallback)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private object CreateCallback: RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            createDefaultTodo()
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }


}