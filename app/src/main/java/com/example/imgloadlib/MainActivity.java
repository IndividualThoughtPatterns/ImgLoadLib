package com.example.imgloadlib;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import imgLoadLibrary.ImgLoadManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toAct2btn = findViewById(R.id.to_act2_btn);
        toAct2btn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });

        Button checkCacheSizeBtn = findViewById(R.id.check_cache_btn);
        checkCacheSizeBtn.setOnClickListener(view -> {
            ImgLoadManager.get().logCacheSize();
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> urlList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            urlList.add("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        }
        for (int i = 0; i < 3; i++) {
            urlList.add("https://i.imgur.com/DvpvklR.png");
        }

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(urlList);
        recyclerView.setAdapter(recyclerAdapter);
    }
}