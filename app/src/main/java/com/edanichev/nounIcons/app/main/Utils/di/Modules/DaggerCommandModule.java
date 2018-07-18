package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DaggerCommandModule {

    @Provides
    @Singleton
    public GetIconsCommand provideGetIconsCommand(Retrofit retrofit) {
        return new GetIconsCommand(retrofit);
    }
}
