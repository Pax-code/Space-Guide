package com.release.spaceguide.adapters.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.release.spaceguide.adapters.data.local.DB
import com.release.spaceguide.adapters.data.local.DBModel
import com.release.spaceguide.adapters.data.repo.ApodRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesPageViewModel(app: Application): AndroidViewModel(app) {

    val apods: LiveData<List<DBModel>>
    val repo: ApodRepo


    init {
        val dao = DB.getDB(app).dbdao()
        repo = ApodRepo(dao)
        apods = repo.allApod
    }

    fun delete(d: DBModel) = viewModelScope.launch(Dispatchers.IO) {
        repo.delete(d)
    }

    fun downloadImage(context: Context, position: Int){
        //download and store image
        apods.value?.let {
            val request = DownloadManager.Request(Uri.parse(it[position].URL))
                .setTitle(it[position].title)
                .setDescription("Downloading image from NASA")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "${it[position].title}.jpg")

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
        }

    }


}