package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.Utils.DB.Realm.IconsRealmAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerDBModule {

    @Provides
    @Singleton
    public IconsRealmAdapter providesIconsRealmAdapter() {
        return new IconsRealmAdapter();
    }
}
