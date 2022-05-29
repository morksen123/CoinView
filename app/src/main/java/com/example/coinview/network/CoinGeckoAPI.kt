package com.example.coinview.network

import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Data class where it holds information on how to serialize / deserialize our data
 *
 * Try deserializing other fields!
 *
 * Note: @field is important!
 */
data class CoinList(
    val coins: List<CoinMeta>
)

//"id":"01coin","symbol":"zoc","name":"01coin"
data class CoinMeta(
    @field:Json(name = "id") var id: String,
    @field:Json(name = "symbol") val symbol: String,
    @field:Json(name = "name") val name: String,
)

data class Price(
    @field: Json(name="usd") val USD : String,
    @field: Json(name="sgd") val SGD : String
)

data class MarketData(
    @field: Json(name="current_price") val current_price: Price
)

data class CoinData(
    @field:Json(name = "id") var id: String,
    @field:Json(name = "symbol") val symbol: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "market_data") val market_data: MarketData
)

data class SupportedCoins(
    val coins : List<String>
)

interface CoinGeckoAPI {
    @GET("coins/list")
    fun getCoinsList(): Call<List<CoinMeta>>
    @GET("simple/supported_vs_currencies")
    fun getSupportCurrencies(): Call<List<String>>
    @GET("coins/{id}")
    fun getCoinData(@Path("id") id:String): Call<CoinData>
}