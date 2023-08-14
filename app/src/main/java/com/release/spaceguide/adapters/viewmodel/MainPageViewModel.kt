package com.release.spaceguide.adapters.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.release.spaceguide.adapters.data.local.DB
import com.release.spaceguide.adapters.data.local.DBModel
import com.release.spaceguide.adapters.data.remote.ApiService
import com.release.spaceguide.adapters.data.remote.apod.ApiModel
import com.release.spaceguide.adapters.data.repo.ApodRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPageViewModel(app: Application) : AndroidViewModel(app) {
    private val apiService = ApiService()
    private val compositeDisposable = CompositeDisposable()

    private val dao = DB.getDB(app).dbdao()
    private var repo: ApodRepo = ApodRepo(dao)

    private val _apiData = MutableLiveData<ApiModel>()
    val apiData: LiveData<ApiModel>
        get() = _apiData

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
        get() = _status

    fun fetchData(){
        compositeDisposable.add(
            apiService.getDataFromService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ApiModel>()
                {
                    override fun onSuccess(t: ApiModel) {
                        _apiData.value = t
                        _status.value = true
                    }

                    override fun onError(e: Throwable) {
                        println("error ${e.message}")
                        _status.value = false
                        println(status)
                    }
                })
        )
    }


    fun getAllApods() : LiveData<List<DBModel>>{
        return repo.allApod
    }

    fun insert(d: DBModel, url: String, title: String) = viewModelScope.launch(Dispatchers.IO){
        d.URL = url
        d.title = title
        repo.insert(d)
    }

    fun delete(d: DBModel) = viewModelScope.launch(Dispatchers.IO) {
        repo.delete(d)
    }

    fun insertImageIfNotExists(image: DBModel) = viewModelScope.launch(Dispatchers.IO) {
        val existingImage = dao.getImageByTitleAndUrl(image.title, image.URL)

        if (existingImage == null) {
            repo.insert(image)
        }
    }

    fun downloadImage(context: Context){
        //download and store image
        apiData.value?.let {
            val request = DownloadManager.Request(Uri.parse(it.hdurl))
                .setTitle(it.title)
                .setDescription("Downloading image from NASA")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "${it.title}.jpg")

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}


