package com.release.spaceguide.adapters.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DBModel::class], version = 1)
abstract class DB: RoomDatabase() {

    abstract fun  dbdao(): DBDao

    companion object{

        @Volatile
        private var INSTANCE: DB? = null

        fun getDB(context: Context): DB{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,
                    "DataBase"
                ).build()
                INSTANCE = instance
                instance

            }
        }
    }
}