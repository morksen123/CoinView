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
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val MYNAME = "MainActivity"
    private val maxNumCoins = 30
    private val coinIds = ArrayList<String>()
    private val coinGeckoAPI: CoinGeckoAPI = NetworkModule.coinGeckoAPI
    private val cryptoImgAPI: CryptoImgAPI = NetworkModule.cryptoImgAPI
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    private var coinList: ArrayList<CoinMeta> = ArrayList<CoinMeta>()
    private val coinListView: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>(maxNumCoins)

    private var supportedCoins: ArrayList<String> = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCoinSymbols()

        val recyclerview = findViewById<RecyclerView>(R.id.coinList)

        recyclerview.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener { getAllCoinData() }

        getAllCoinData()
        recyclerview.adapter = CustomAdapter(coinListView)

    }

    private fun initCoinSymbols(){
        coinIds.add("bitcoin")
        coinIds.add("ethereum")
        coinIds.add("eth2-staking-by-poolx")
        coinIds.add("tether")
        coinIds.add("usd-coin")
        coinIds.add("binancecoin")
        coinIds.add("ripple")
        coinIds.add("cardano")
        coinIds.add("solana")
        coinIds.add("dogecoin")
        coinIds.add("polkadot")
        coinIds.add("wrapped-bitcoin")

    }

    private fun getAllCoinData(){
        coinIds.forEachIndexed { idx, coin ->
            run {
                getCoinData(coin)
            }
        }
    }

    private fun getCoinData(id: String){
        coinGeckoAPI.getCoinData(id)
            .enqueue(object : Callback<CoinData>{
                override fun onResponse(call: Call<CoinData>, response: Response<CoinData>){
                    Log.i(MYNAME, "getCoinData $id")
                    val coinData = response.body() ?: return
                    val imgUri = "@drawable/$id.png"
                    val imgRes = resources.getIdentifier(imgUri, null, packageName)
                    coinListView.add(ItemsViewModel(imgRes, id, "$" + coinData.market_data.current_price.USD.toString()))
                }
                override fun onFailure(call: Call<CoinData>, t: Throwable){
                    Log.e(MYNAME, "getCoinData onFailure() $id")
                    t.printStackTrace()
                }
            })
    }

    private fun getCoinImg(sym: String, size: Long = 64, idx: Int = 0){
        cryptoImgAPI.getCoinImg(sym, size)
            .enqueue(object : Callback<Image> {
                override fun onResponse(call: Call<Image>, response: Response<Image>){
                    Log.i(MYNAME, "getCoinImg onResponse()")
                    val coinImg = response.body() ?: return
//                    coinListView[idx].image = coinImg
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
                        coinListView[idx].name = x.name
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