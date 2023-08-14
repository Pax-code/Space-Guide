package com.release.spaceguide.adapters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.release.spaceguide.adapters.data.remote.ApiService
import com.release.spaceguide.adapters.data.remote.planetsimage.ImageApiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ExploreViewModel: ViewModel() {

    private val apiService = ApiService()
    private val compositeDisposable = CompositeDisposable()

    private val _apiData = MutableLiveData<List<String>>()
    val apiData: LiveData<List<String>>
        get() = _apiData

    private val _galaxyURL = MutableLiveData<List<String>>()
    val galaxyURL: LiveData<List<String>>
        get() = _galaxyURL

    private val _titles = MutableLiveData<List<String>>()
    val titles: LiveData<List<String>>
        get() = _titles

    private val _descriptions = MutableLiveData<List<String>>()
    val descriptions: LiveData<List<String>>
        get() = _descriptions

    private val queryList = listOf(
        "space",
        "galaxies",
        "stars",
        "nebulas",
        "astronomy",
        "cosmos",
        "asteroid",
        "black+holes",
        "telescopes"
    )
    private var queryIndex = 0
    fun fetchData(){

        compositeDisposable.add(
            apiService.getImages("planets")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<ImageApiModel>(){

                    override fun onNext(t: ImageApiModel) {
                        val links = mutableListOf<String>()
                        t.collection.items.forEach { item ->
                            item.links.firstOrNull()?.href?.let { link ->
                                links.add(link)
                            }
                        }
                        _apiData.value = links
                    }

                    override fun onError(e: Throwable) {
                        println("error ${e.message}")
                    }

                    override fun onComplete() {
                        println("success")
                    }
                })
        )
    }


    fun fetchGalaxiesData(){
        val query = queryList.getOrElse(queryIndex) { "galaxies" }

        if (query.isNotEmpty()) {
            compositeDisposable.add(
                apiService.getImages(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<ImageApiModel>() {


                        override fun onNext(t: ImageApiModel) {

                            val descrition = mutableListOf<String>()
                            t.collection.items.forEach { item ->
                                item.data.firstOrNull()?.title?.let { title ->
                                    descrition.add(title)
                                }
                            }

                            _descriptions.value = descrition


                            val titles = mutableListOf<String>()
                            t.collection.items.forEach { item ->
                                item.data.firstOrNull()?.center?.let { title ->
                                    titles.add(title)
                                }
                            }

                            _titles.value = titles


                            val links = mutableListOf<String>()
                            t.collection.items.forEach { item ->
                                item.links.firstOrNull()?.href?.let { link ->
                                    links.add(link)
                                }
                            }
                            _galaxyURL.value = links

                        }

                        override fun onError(e: Throwable) {
                            println("error ${e.message}")
                        }

                        override fun onComplete() {
                            println("success")
                        }
                    })
            )

        }
        queryIndex = (queryIndex + 1) % queryList.size
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}