package com.edanichev.nounIcons.app.main.Utils.di.Component;

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounHintCloud.Presenter.HintCloudPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerApplicationContextModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerConfigModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerDBModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerInteractorModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerNetworkModule;
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
        DaggerApplicationContextModule.class
})
public interface AppComponent {
    void inject(GetIconsCommand target);

    void inject(MainActivity activity);

    INounSharedPreferences prefs();

    IHintCloudInteractor interactor();

    HintCloudPresenter hintCloudPresenter();

    INounConfig config();
}
