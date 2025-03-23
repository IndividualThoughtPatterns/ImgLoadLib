package com.example.imgloadlib;

import android.content.Intent;
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

        Button btn = findViewById(R.id.btn2);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });
        ImageView imageView = findViewById(R.id.imgView2);

        Button btn2 = findViewById(R.id.btn2);

        Button btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(view -> {
            Log.d("mydebug", "eviction count: " + ImgLoadManager.with().getEvictionCount());
        });
        btn2.setOnClickListener(view -> {

            ArrayList<String> urlList = new ArrayList<>();

//            for (int i = 1; i < 1000; i++) {
//                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + i + ".png";
//                urlList.add(url);
//            }
//            for (int i = 1; i < 1000; i++) {
//                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + i + ".png";
//                urlList.add(url);
//            }
//            for (int i = 1; i < 1000; i++) {
//                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + i + ".png";
//                urlList.add(url);
//            }
//            for (int i = 1; i < 1000; i++) {
//                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + i + ".png";
//                urlList.add(url);
//            }
            for (int i = 1; i < 1000; i++) {
                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + i + ".png";
                urlList.add(url);
            }
            for (int i = 1; i < 1000; i++) {
                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/" + i + ".png";
                urlList.add(url);
            }
            for (int i = 1; i < 1000; i++) {
                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + i + ".png";
                urlList.add(url);
            }
            for (int i = 1; i < 1000; i++) {
                String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/" + i + ".png";
                urlList.add(url);
            }

            for (int i = 0; i < urlList.size(); i++) {
                ImgLoadManager.with().load(urlList.get(i)).into(imageView);
            }
        });
    }
}