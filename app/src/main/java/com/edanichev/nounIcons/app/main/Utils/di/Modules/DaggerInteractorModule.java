package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.HintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.ISearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.SearchIconsInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerInteractorModule {

    @Provides
    @Singleton
    public IHintCloudInteractor provideIHintCloudInteractor() {
        return new HintCloudInteractor();
    }

    @Provides
    @Singleton
    public ISearchIconsInteractor provideISearchIconsInteractor() {
        return new SearchIconsInteractor();
    }

}