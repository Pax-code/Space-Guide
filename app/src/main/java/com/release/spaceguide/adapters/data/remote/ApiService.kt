package com.release.spaceguide.adapters.data.remote

import com.release.spaceguide.adapters.BASE_URL
import com.release.spaceguide.adapters.IMAGES_BASE_URL
import com.release.spaceguide.adapters.data.remote.apod.ApiModel
import com.release.spaceguide.adapters.data.remote.planetsimage.ImageApiModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(Api::class.java)

    private val images = Retrofit.Builder()
        .baseUrl(IMAGES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(Api::class.java)



    fun getDataFromService(): Single<ApiModel> {
        return api.getApod()
    }

    fun getImages(query:String): Observable<ImageApiModel> {
        return images.getImages(query)
    }


}