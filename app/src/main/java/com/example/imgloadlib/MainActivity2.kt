package com.example.imgloadlib

import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import imgLoadLibrary.ImgLoadManager

class MainActivity2 : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_main2)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imageView2 = findViewById<ImageView>(R.id.imageView2)

        val checkCacheSizeBtn = findViewById<Button>(R.id.check_cache_btn)
        checkCacheSizeBtn.setOnClickListener {
            ImgLoadManager.get()?.logCacheSize()
        }

        val downloadBigAmountOfImagesBtn =
            findViewById<Button>(R.id.download_big_amount_of_images_btn)
        downloadBigAmountOfImagesBtn.setOnClickListener {
            val urlList = ArrayList<String>()
            for (i in 1..999) {
                val url1 =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$i.png"
                val url2 =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/$i.png"
                val url3 =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$i.png"
                val url4 =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/$i.png"

                urlList.add(url1)
                urlList.add(url2)
                urlList.add(url3)
                urlList.add(url4)
            }
            for (i in urlList.indices) {
                ImgLoadManager.get()?.load(urlList[i], imageView, i)
            }
        }

        val downloadSmallAmountOfImagesBtn =
            findViewById<Button>(R.id.download_small_amount_of_images_btn)
        downloadSmallAmountOfImagesBtn.setOnClickListener {
            val urlList = ArrayList<String>()
            for (i in 1..10) {
                val url =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + 1 + ".png"
                urlList.add(url)
            }
            for (i in urlList.indices) {
                ImgLoadManager.get()?.load(urlList[i], imageView, i)
                ImgLoadManager.get()?.load(urlList[i], imageView2, i)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("mydebug", "back pressed")
    }
}