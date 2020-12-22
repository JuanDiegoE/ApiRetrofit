package com.example.pruebretrofit

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("/0.2/languages")
    suspend fun getLenguages(): Response<List<Lenguage>>

    @Headers("Authorization: Bearer 4b16bbf0ea55ae6abcd78ce036d1a485")
    @FormUrlEncoded
    @POST("/0.2/detect")
    suspend fun getTextLanguage(@Field("q")text:String) : Response<DetectionResponse>
}