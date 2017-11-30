package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.HintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.ISearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.SearchIconsInteractor;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IGetIconsCommand;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@Module
public class DaggerInteractorModule {

    @Provides
    @Singleton
    public IHintCloudInteractor provideIHintCloudInteractor() {
        return new HintCloudInteractor();
    }

    @Provides
    @Singleton
    public ISearchIconsInteractor provideISearchIconsInteractor(CompositeDisposable compositeDisposable, IGetIconsCommand command) {
        return new SearchIconsInteractor(compositeDisposable, command);
    }

}
