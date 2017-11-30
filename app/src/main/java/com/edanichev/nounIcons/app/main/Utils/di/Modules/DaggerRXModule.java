package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@Module
public class DaggerRXModule {

    @Provides
    @Singleton
    public CompositeDisposable provideCompositeSubscription() {
        return new CompositeDisposable();
    }

}
