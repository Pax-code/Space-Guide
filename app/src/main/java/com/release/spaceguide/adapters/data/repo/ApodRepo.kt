package com.release.spaceguide.adapters.data.repo

import androidx.lifecycle.LiveData
import com.release.spaceguide.adapters.data.local.DBDao
import com.release.spaceguide.adapters.data.local.DBModel

class ApodRepo (private val dbDao: DBDao){

    val allApod: LiveData<List<DBModel>> = dbDao.getFavoriteImages()


    suspend fun insert(dbModel: DBModel){
        dbDao.insert(dbModel)
    }

    suspend fun delete(dbModel: DBModel){
        dbDao.delete(dbModel)
    }

}