package com.example.coinview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinview.network.CoinGeckoAPI
import com.example.coinview.network.NetworkModule
import com.example.coinview.network.SupportedCoins
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val MYNAME = "MainActivity"
    private val coinGeckoAPI: CoinGeckoAPI = NetworkModule.coinGeckoAPI
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.coinList)

        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<ItemsViewModel>()

        val coinArray = ArrayList<ItemsViewModel>()

        // bitcoin, eth, dopecoin, bitcoin2x, blocknet, candy, chess, coupe, dascoin, educare
        data.add(ItemsViewModel(R.drawable.bitcoin2x, text = "Bitcoin2x"))
        data.add(ItemsViewModel(R.drawable.blocknet, text = "blocknet"))
        data.add(ItemsViewModel(R.drawable.candy, text = "candy"))
        data.add(ItemsViewModel(R.drawable.chesscoin, text = "chesscoin"))
        data.add(ItemsViewModel(R.drawable.coupecoin, text = "coupecoin"))
        data.add(ItemsViewModel(R.drawable.dascoin, text = "dascoin"))
        data.add(ItemsViewModel(R.drawable.dopecoin, text = "dopecoin"))
        data.add(ItemsViewModel(R.drawable.educare, text = "educare"))
        data.add(ItemsViewModel(R.drawable.aelf, text = "aelf"))
        data.add(ItemsViewModel(R.drawable.adbank, text = "adbank"))
        data.add(ItemsViewModel(R.drawable.acchain, text = "acchain"))
        data.add(ItemsViewModel(R.drawable.ace, text = "ace"))
        data.add(ItemsViewModel(R.drawable.apis, text = "apis"))
        data.add(ItemsViewModel(R.drawable.aidoc, text = "aidoc"))
        data.add(ItemsViewModel(R.drawable.ardor, text = "ardor"))

        val adapter = CustomAdapter(data)

        recyclerview.adapter = adapter

        fab.setOnClickListener { getData() }

        getData()
    }

    private fun getData() {
        coinGeckoAPI.getSupportCurrencies()
            .enqueue(object : Callback<SupportedCoins> {
                override fun onResponse(call: Call<SupportedCoins>, response: Response<SupportedCoins>){

                }
                override fun onFailure(call: Call<SupportedCoins>,  t: Throwable){
                    Log.e(MYNAME, "getData onFailure()")
                }
            })
    }
}