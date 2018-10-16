package com.musasyihab.simplenews.api

import com.musasyihab.simplenews.BuildConfig
import com.musasyihab.simplenews.model.GetNewsModel
import com.musasyihab.simplenews.model.GetSourcesModel
import com.musasyihab.simplenews.util.Constants
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {

    @GET("sources?language=en")
    fun getSourceList(): Observable<GetSourcesModel>

    @GET("everything?language=en")
    fun getNewsList(@Query(value="sources") sources: String, @Query(value="pageSize") pageSize: Int, @Query(value="q") query: String): Observable<GetNewsModel>

    companion object Factory {
        fun create(): ApiServiceInterface {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
            }
            builder.addInterceptor({ chain ->
                val request = chain.request().newBuilder().addHeader("X-Api-Key", BuildConfig.NewsapiAPIKey).build()
                chain.proceed(request)
            })
            val client = builder.build()
            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .build()

            return retrofit.create(ApiServiceInterface::class.java)
        }
    }
}