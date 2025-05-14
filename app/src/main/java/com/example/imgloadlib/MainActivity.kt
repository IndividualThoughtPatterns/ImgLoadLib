package com.example.imgloadlib

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import imgLoadLibrary.ImgLoadManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toAct2btn = findViewById<Button>(R.id.to_act2_btn)
        toAct2btn.setOnClickListener { view: View? ->
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val checkCacheSizeBtn = findViewById<Button>(R.id.check_cache_btn)
        checkCacheSizeBtn.setOnClickListener { view: View? ->
            ImgLoadManager.get()?.logCacheSize()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val urlList = ArrayList<String>()
        for (i in 0..2) {
            urlList.add("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png")
        }
        for (i in 0..2) {
            urlList.add("https://i.imgur.com/DvpvklR.png")
        }

        val recyclerAdapter = RecyclerAdapter(urlList)
        recyclerView.adapter = recyclerAdapter
        Log.d("mydebug", "\n")
    }
}