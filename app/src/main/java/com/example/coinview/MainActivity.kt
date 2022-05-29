package com.example.coinview

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinview.models.internalCoinData
import com.example.coinview.network.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val MYNAME = "MainActivity"
    private val maxNumCoins = 30
    private val coinIds = ArrayList<internalCoinData>()
    private val coinGeckoAPI: CoinGeckoAPI = NetworkModule.coinGeckoAPI
    private val cryptoImgAPI: CryptoImgAPI = NetworkModule.cryptoImgAPI
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    private val recyclerview by lazy { findViewById<RecyclerView>(R.id.coinList) }
    private var coinList: ArrayList<CoinMeta> = ArrayList<CoinMeta>()
    private val coinListView: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>(maxNumCoins)

    private var supportedCoins: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCoinSymbols()
        recyclerview.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener { getAllCoinData() }

        getAllCoinData()
        recyclerview.adapter = CustomAdapter(coinListView)

    }

    private fun initCoinSymbols(){
        coinIds.add(internalCoinData("bitcoin", "Bitcoin"))
        coinIds.add(internalCoinData("ethereum", "Ethereum"))
        coinIds.add(internalCoinData("eth2-staking-by-poolx", "Ethereum 2"))
        coinIds.add(internalCoinData("tether", "Tether"))
        coinIds.add(internalCoinData("usd-coin", "USD Coin"))
        coinIds.add(internalCoinData("binancecoin", "BNB"))
        coinIds.add(internalCoinData("ripple", "XRP"))
        coinIds.add(internalCoinData("cardano", "Cardano"))
        coinIds.add(internalCoinData("solana", "Solana"))
        coinIds.add(internalCoinData("dogecoin", "Dogecoin"))
        coinIds.add(internalCoinData("polkadot", "Polkadot"))
        coinIds.add(internalCoinData("wrapped-bitcoin", "Wrapped Bitcoin"))
        coinIds.add(internalCoinData("tron", "TRON"))
        coinIds.add(internalCoinData("avalanche-2", "Avax"))
        coinIds.add(internalCoinData("dai","dai"))
        coinIds.add(internalCoinData("shiba-inu", "Shiba Inu"))
        coinIds.add(internalCoinData("matic-network", "Polygon"))
        coinIds.add(internalCoinData("ftx-token", "FTX Token"))
        coinIds.add(internalCoinData("near", "NEAR Protocol"))
        coinIds.add(internalCoinData("uniswap", "Uniswap"))
        coinIds.add(internalCoinData("bitcoin-cash", "Bitcoin Cash"))
        coinIds.add(internalCoinData("link", "Link"))
    }

    private fun getAllCoinData(){
        coinIds.forEachIndexed { idx, coin ->
            run {
                getCoinData(coin)
            }
        }
    }

    private fun getCoinData(q: internalCoinData){
        val id = q.id
        val name = q.name
        coinGeckoAPI.getCoinData(id)
            .enqueue(object : Callback<CoinData>{
                override fun onResponse(call: Call<CoinData>, response: Response<CoinData>){
                    Log.i(MYNAME, "getCoinData $id")
                    val coinData = response.body() ?: return
                    val imgName = id.replace("-","_")
                    val imgUri = "@drawable/$imgName"
                    Log.i(MYNAME, imgUri)
//                    resources.getIdentifier()
                    var imgRes = resources.getIdentifier(imgUri, "drawable", packageName)
                    Log.i(MYNAME, coinData.market_data.current_price.USD)
                    Log.i(MYNAME, coinListView.size.toString())

                    coinListView.add(ItemsViewModel(imgRes, name, "$" + coinData.market_data.current_price.USD))
                    Log.i(MYNAME, "Added to coinListView $id")
                    recyclerview.adapter?.notifyDataSetChanged()
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