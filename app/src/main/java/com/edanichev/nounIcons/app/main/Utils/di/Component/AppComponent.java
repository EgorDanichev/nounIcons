package com.edanichev.nounIcons.app.main.Utils.di.Component;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerDBModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerNetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        DaggerNetworkModule.class,
        DaggerDBModule.class
})
public interface AppComponent {
    void inject(GetIconsCommand target);
}
