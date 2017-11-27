package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig;
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.NounConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerConfigModule {

    @Provides
    @Singleton
    public INounConfig provideINounBuildConfig() {
        return NounConfig.getInstance();
    }
}
