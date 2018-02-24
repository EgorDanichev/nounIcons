package com.edanichev.nounIcons.app.main.Utils.di.Component;

import com.edanichev.nounIcons.app.main.NounIconsList.Model.SearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerApplicationContextModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerCommandModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerConfigModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerDBModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerInteractorModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerNetworkModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerRXModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerSharedPreferencesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        DaggerNetworkModule.class,
        DaggerDBModule.class,
        DaggerSharedPreferencesModule.class,
        DaggerInteractorModule.class,
        DaggerConfigModule.class,
        DaggerApplicationContextModule.class,
        DaggerRXModule.class,
        DaggerCommandModule.class
})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(SearchIconsInteractor interactor);

//    INounSharedPreferences prefs();
//
//    IHintCloudInteractor interactor();
//
//    HintCloudPresenter hintCloudPresenter();
//
//    INounConfig config();
}
