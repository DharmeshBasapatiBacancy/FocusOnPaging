package com.example.focusonpaging.network.service

import com.example.focusonpaging.network.model.ProductSearchResponseBody
import com.example.focusonpaging.network.model.Repo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApiService {

    @GET("users/{username}/repos")
    suspend fun fetchRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): List<Repo>

    @Headers( "X-TYPESENSE-API-KEY:zBLij8ihv6XHqz6kpSAsokmxmbdBxWuR")
    @GET("https://0gkh49werov6qtu1p-1.a1.typesense.net/collections/staffapp_product_v1/documents/search")
    suspend fun searchProduct(
        @Query("q") query: String = "*",
        @Query("query_by") queryBy: String = "brand_name",
        @Query("filter_by") filterBy: String = "",
        @Query("facet_by") facetBy: String = "",
        @Query("include_fields") includeFields: String = "",
        @Query("page") pageNo: Int = 1
    ): Response<ProductSearchResponseBody>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): MyApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.HEADERS

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
//                .addInterceptor(
//                    Interceptor { chain ->
//                        val requestBuilder = chain.request().newBuilder()
//                        requestBuilder.addHeader("X-TYPESENSE-API-KEY", "zBLij8ihv6XHqz6kpSAsokmxmbdBxWuR")
//                        return@Interceptor chain.proceed(requestBuilder.build())
//                    }
//                )
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApiService::class.java)
        }
    }
}