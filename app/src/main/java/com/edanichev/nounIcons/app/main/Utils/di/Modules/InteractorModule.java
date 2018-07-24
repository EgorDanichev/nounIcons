package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.HintCloudInteractor;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.edanichev.nounIcons.app.main.iconlist.model.SearchIconsInteractor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class InteractorModule {

    @Provides
    public HintCloudInteractor provideIHintCloudInteractor() {
        return new HintCloudInteractor();
    }

    @Provides
    public SearchIconsInteractor provideISearchIconsInteractor(CompositeDisposable compositeDisposable, GetIconsCommand command) {
        return new SearchIconsInteractor(command, compositeDisposable);
    }
}