package com.release.spaceguide.adapters.data.remote


import com.release.spaceguide.adapters.API_KEY
import com.release.spaceguide.adapters.data.remote.apod.ApiModel
import com.release.spaceguide.adapters.data.remote.planetsimage.ImageApiModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("planetary/apod?api_key=${API_KEY}")
    fun getApod():Single<ApiModel>


    //end point = https://images-api.nasa.gov/search?media_type=image&page=1&page_size=20&q=planets
    @GET("search?media_type=image&page=1&page_size=50")
    fun getImages(@Query("q") query: String): Observable<ImageApiModel>

}