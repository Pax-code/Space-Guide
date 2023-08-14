package com.release.spaceguide.adapters.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class DBModel (

    @ColumnInfo(name="title")
    var title: String,

    @ColumnInfo(name="URL")
    var URL: String

){

    @PrimaryKey(autoGenerate = true)
    var id = 0

}