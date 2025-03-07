package com.example.imgloadlib;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.MalformedURLException;

import imgLoadLibrary.ImgLoadManager;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView imageView = findViewById(R.id.img2);

        Button btn = findViewById(R.id.btn2);
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
                    "https://i.imgur.com/DvpvklR.png");
        } catch (MalformedURLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}