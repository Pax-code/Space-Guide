package com.release.spaceguide.adapters.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DBDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(DBModel: DBModel)

    @Delete
    suspend fun delete(DBModel: DBModel)

    @Query("SELECT * FROM DBModel WHERE title = :title AND URL = :url")
    suspend fun getImageByTitleAndUrl(title: String, url: String): DBModel?

    @Query("SELECT * FROM DBModel ORDER BY id ASC")
    fun getFavoriteImages(): LiveData<List<DBModel>>
}