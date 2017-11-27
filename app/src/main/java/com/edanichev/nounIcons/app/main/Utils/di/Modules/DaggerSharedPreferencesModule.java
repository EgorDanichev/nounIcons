package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.NounSharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerSharedPreferencesModule {

    @Provides
    @Singleton
    public INounSharedPreferences providesNounSharedPreferences() {
        return NounSharedPreferences.getInstance();
    }
}
