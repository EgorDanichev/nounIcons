package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import android.content.Context;

import com.edanichev.nounIcons.app.main.NounApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerApplicationContextModule {

    @Provides
    @Singleton
    public Context provideContext() {
        return NounApp.app().getApplicationContext();
    }
}
