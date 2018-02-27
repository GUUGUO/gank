package com.guuguo.gank.net

import com.google.gson.GsonBuilder
import com.guuguo.android.pikacomic.net.https.TrustAllCerts
import com.guuguo.gank.constant.dataPattern
import com.guuguo.gank.model.GankDays
import com.guuguo.gank.model.GankNetResult
import com.guuguo.gank.model.Ganks
import com.guuguo.gank.model.entity.GankModel
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by gaohailong on 2016/5/17.
 */
interface Service {
    @GET("data/{type}/{count}/{page}")
    fun getGanHuo(@Path("type") type: String, @Path("count") count: Int, @Path("page") page: Int)
            : Single<Ganks<ArrayList<GankModel>>>

    @GET("day/{date}")
    fun getGankOneDay(@Path("date") date: String): Single<GankDays>

    @GET("search/query/{query}/category/{category}/count/{count}/page/{page} ")
    fun getGankSearchResult(@Path("query") query: String, @Path("category") category: String, @Path("count") count: Int, @Path("page") page: Int): Single<GankNetResult>

    companion object {
        private const val GANHUO_API = "https://gank.io/api/"
        fun create(): Service = create(HttpUrl.parse(GANHUO_API)!!)
        fun create(httpUrl: HttpUrl): Service {

            val commonHttpBuilder = OkHttpClient.Builder()
                    .sslSocketFactory(TrustAllCerts.createSSLSocketFactory())
                    .hostnameVerifier(TrustAllCerts.TrustAllHostnameVerifier())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
            val httpClient = commonHttpBuilder
                    .retryOnConnectionFailure(false)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(httpClient)
                    .addConverterFactory(getGsonConverter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(Service::class.java)
        }

        fun getGsonConverter(): GsonConverterFactory {
            return GsonConverterFactory
                    .create(GsonBuilder().setDateFormat(dataPattern).create())
        }
    }
}
