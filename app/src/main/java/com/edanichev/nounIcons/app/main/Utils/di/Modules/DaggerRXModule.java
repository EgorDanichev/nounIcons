package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class DaggerRXModule {

    @Provides
    @Singleton
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}