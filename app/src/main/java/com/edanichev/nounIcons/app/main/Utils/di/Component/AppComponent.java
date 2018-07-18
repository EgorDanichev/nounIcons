package com.edanichev.nounIcons.app.main.Utils.di.Component;

import com.edanichev.nounIcons.app.main.Utils.di.Modules.InteractorModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.NetworkModule;
import com.edanichev.nounIcons.app.main.iconlist.view.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerAnalyticsModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerApplicationContextModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerCommandModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerConfigModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerDBModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerRXModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerSharedPreferencesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class,
        DaggerDBModule.class,
        DaggerSharedPreferencesModule.class,
        InteractorModule.class,
        DaggerConfigModule.class,
        DaggerApplicationContextModule.class,
        DaggerRXModule.class,
        DaggerCommandModule.class,
        DaggerAnalyticsModule.class
})
public interface AppComponent {

    void inject(MainActivity activity);
}