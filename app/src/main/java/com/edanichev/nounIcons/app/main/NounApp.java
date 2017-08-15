package com.edanichev.nounIcons.app.main;

import android.app.Application;

import com.edanichev.nounIcons.app.main.Utils.Auth.TokenStorage;

public class NounApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        TokenStorage.initInstance(this);
    }

}
