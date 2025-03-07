package com.example.imgloadlib;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;

import imgLoadLibrary.ImgLoadManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView imageView = findViewById(R.id.img);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });

        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(view -> {
            ImgLoadManager.with().logHashMapSize();
        });

        try {
            ImgLoadManager.with().load(imageView,
                    "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        } catch (MalformedURLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}