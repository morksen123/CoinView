package com.example.coinview.network

import android.media.Image
import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoImgAPI {
    @GET("{sym}/{size}")
    fun getCoinImg(@Path("sym")sym: String, @Path("size")size: Long = 64): Call<Image>
}