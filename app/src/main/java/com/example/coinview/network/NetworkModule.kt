package com.example.coinview.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton so we do not recreate multiple instances
 *
 * Network code are expensive so we try to keep a minimal instance of them
 */
object NetworkModule {
    // Create and config an instance of retrofit
    private val cg_retro = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .addConverterFactory(MoshiConverterFactory.create()) //KotlinJsonAdapterFactory
        .build()

    private val c_img_retro = Retrofit.Builder()
        .baseUrl("https://cryptoicons.org/api/icon/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    // Create our API
    val coinGeckoAPI: CoinGeckoAPI = cg_retro.create(CoinGeckoAPI::class.java)
    val cryptoImgAPI: CryptoImgAPI = c_img_retro.create(CryptoImgAPI::class.java)
}