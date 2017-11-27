package com.edanichev.nounIcons.app.main;

import android.app.Application;

import com.edanichev.nounIcons.app.main.Utils.DB.Firebase.FirebaseAdapter;
import com.edanichev.nounIcons.app.main.Utils.DB.Realm.RealmMigration;
import com.edanichev.nounIcons.app.main.Utils.EventBus.NounApiConfigEvent;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.NounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.di.Component.AppComponent;
import com.edanichev.nounIcons.app.main.Utils.di.Component.DaggerAppComponent;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerDBModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerNetworkModule;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NounApp extends Application {
    private final static String BASE_NOUN_URL = "http://api.thenounproject.com/";
    private AppComponent component;
    private static NounApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        NounSharedPreferences.initInstance(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .migration(new RealmMigration())
                .build();
        Realm realm = Realm.getInstance(config);
        Realm.setDefaultConfiguration(config);
        realm.close();

        EventBus.builder()
                .sendNoSubscriberEvent(false)
                .installDefaultEventBus();

        EventBus.getDefault().register(this);
        FirebaseAdapter.getConfigKey();

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());

//        realm.beginTransaction();
//        realm.deleteAll();
//        realm.commitTransaction();

    }

    public static NounApp app() {
        return app;
    }

    public AppComponent getComponent() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                    .daggerNetworkModule(new DaggerNetworkModule(BASE_NOUN_URL))
                    .daggerDBModule(new DaggerDBModule())
                    .build();
        }
        return this.component;
    }

    @Subscribe
    public void onNounApiConfigResponse(NounApiConfigEvent event) {
        NounSharedPreferences.getInstance().setNounApiConfig(event.getNoun_key(), event.getNoun_secret());
    }
}
