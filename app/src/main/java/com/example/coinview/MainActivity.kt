package com.example.coinview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        for (i in 1..20){
            data.add(ItemsViewModel(R.drawable.ic_launcher_foreground, "Item " + i))
        }

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