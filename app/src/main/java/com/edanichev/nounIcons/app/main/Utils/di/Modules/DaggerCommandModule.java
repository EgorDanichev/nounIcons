package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.Utils.DB.Realm.IconsRealmAdapter;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IGetIconsCommand;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DaggerCommandModule {

    @Provides
    @Singleton
    public IGetIconsCommand provideGetIconsCommand(Retrofit retrofit, IconsRealmAdapter adapter) {
        return new GetIconsCommand(retrofit, adapter);
    }
}
