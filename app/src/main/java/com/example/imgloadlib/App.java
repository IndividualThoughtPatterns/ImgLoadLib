package com.example.imgloadlib;

import android.app.Application;

import imgLoadLibrary.ImgLoadManager;

public class App extends Application {
    public static App instance = null;
    public ImgLoadManager imgLoadManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        imgLoadManager = new ImgLoadManager();
    }
}
