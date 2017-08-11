package com.antonioleiva.mvpexample.app.main;

import android.app.Application;

import com.antonioleiva.mvpexample.app.main.NounIconsList.TokenStorage;

public class NounApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        TokenStorage.initInstance(this);
    }

}
