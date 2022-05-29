package com.example.coinview

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinview.network.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val MYNAME = "MainActivity"
    private val maxNumCoins = 50
    private val coinGeckoAPI: CoinGeckoAPI = NetworkModule.coinGeckoAPI
    private val cryptoImgAPI: CryptoImgAPI = NetworkModule.cryptoImgAPI
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    private var coinList: ArrayList<CoinMeta> = ArrayList<CoinMeta>()
    private val coinListView: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>(maxNumCoins)

    private var supportedCoins: ArrayList<String> = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.coinList)

        recyclerview.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener { getSupportedCoins() }

        getSupportedCoins()
        getCoinList()


        recyclerview.adapter = CustomAdapter(coinListView)
    }

    private fun getCoinImg(sym: String, size: Long = 64, idx: Int = 0){
        cryptoImgAPI.getCoinImg(sym, size)
            .enqueue(object : Callback<Image> {
                override fun onResponse(call: Call<Image>, response: Response<Image>){
                    Log.i(MYNAME, "getCoinImg onResponse()")
                    val coinImg = response.body() ?: return
                    coinListView[idx].image = coinImg
                }
                override fun onFailure(call: Call<Image>, t: Throwable){
                    Log.e(MYNAME, "getCoinImg onFailure()")
                    t.printStackTrace()
                }
            }
        )
    }

    private fun getCoinList(){
        coinGeckoAPI.getCoinsList()
            .enqueue(object : Callback<List<CoinMeta>> {
                override fun onResponse(call: Call<List<CoinMeta>>, response: Response<List<CoinMeta>>){
                    Log.i(MYNAME, "getCoinList onResponse()")
                    val coinMeta = response.body() ?: return
                    coinList = coinMeta.subList(0,maxNumCoins - 1) as ArrayList<CoinMeta>
                    coinList.forEachIndexed  { idx, x ->
                        coinListView[idx].text = x.name
                    }

                }
                override fun onFailure(call: Call<List<CoinMeta>>, t: Throwable){
                    Log.e(MYNAME, "getCoinList onFailure()")
                    t.printStackTrace()
                }
            }
        )
    }

    private fun getSupportedCoins() {

        coinGeckoAPI.getSupportCurrencies()
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>){
                    Log.i(MYNAME, "getSupportedCoins onResponse()")
                    supportedCoins = (response.body() ?: return) as ArrayList<String>
                }
                override fun onFailure(call: Call<List<String>>,  t: Throwable){
                    Log.e(MYNAME, "getSupportedCoins onFailure()")
                    t.printStackTrace()
                }
            }
        )
    }
}