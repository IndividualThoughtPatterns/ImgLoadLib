package com.example.imgloadlib;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import imgLoadLibrary.ImgLoader;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageView imageView = findViewById(R.id.img);
        new ImgLoader(
                imageView,
                "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
        ).load();
    }
}