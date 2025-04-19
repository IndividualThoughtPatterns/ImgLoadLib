package com.example.imgloadlib;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import imgLoadLibrary.ImgLoadManager;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main2);

        ImageView imageView = findViewById(R.id.imageView);
        ImageView imageView2 = findViewById(R.id.imageView2);

        Button checkCacheSizeBtn = findViewById(R.id.check_cache_btn);
        checkCacheSizeBtn.setOnClickListener(view -> {
            ImgLoadManager.get().logCacheSize();
        });

        Button downloadBigAmountOfImagesBtn = findViewById(R.id.download_big_amount_of_images_btn);
        downloadBigAmountOfImagesBtn.setOnClickListener(view -> {

            ArrayList<String> urlList = new ArrayList<>();

            for (int i = 1; i < 1000; i++) {
                String url1 = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + i + ".png";
                String url2 = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/" + i + ".png";
                String url3 = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + i + ".png";
                String url4 = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/" + i + ".png";

                urlList.add(url1);
                urlList.add(url2);
                urlList.add(url3);
                urlList.add(url4);
            }

            for (int i = 0; i < urlList.size(); i++) {
                ImgLoadManager.get().load(urlList.get(i), imageView);
            }
        });

        Button downloadSmallAmountOfImagesBtn = findViewById(R.id.download_small_amount_of_images_btn);
        downloadSmallAmountOfImagesBtn.setOnClickListener(view -> {
            ArrayList<String> urlList = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + 1 + ".png";
                urlList.add(url);
            }
            for (int i = 0; i < urlList.size(); i++) {
                ImgLoadManager.get().load(urlList.get(i), imageView);
                ImgLoadManager.get().load(urlList.get(i), imageView2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("mydebug", "back pressed");
    }
}