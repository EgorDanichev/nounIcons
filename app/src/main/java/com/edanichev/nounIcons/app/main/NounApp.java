package com.edanichev.nounIcons.app.main;

import android.app.Application;

import com.edanichev.nounIcons.app.main.Utils.DB.Firebase.FirebaseAdapter;
import com.edanichev.nounIcons.app.main.Utils.EventBus.NounApiConfigEvent;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.NounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.di.Component.AppComponent;
import com.edanichev.nounIcons.app.main.Utils.di.Component.DaggerAppComponent;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerDBModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.NetworkModule;
import com.edanichev.nounIcons.app.main.Utils.di.Modules.DaggerRXModule;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;

public class NounApp extends Application {
    private final static String BASE_NOUN_URL = "http://api.thenounproject.com/";
    private AppComponent component;
    private FirebaseAnalytics analytics;

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

        EventBus.builder()
                .sendNoSubscriberEvent(false)
                .installDefaultEventBus();

        EventBus.getDefault().register(this);
        FirebaseAdapter.getConfigKey();

        analytics = FirebaseAnalytics.getInstance(this);

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
                    .networkModule(new NetworkModule(BASE_NOUN_URL))
                    .daggerDBModule(new DaggerDBModule())
                    .daggerRXModule(new DaggerRXModule())
                    .build();
        }
        return this.component;
    }

    @Subscribe
    public void onNounApiConfigResponse(NounApiConfigEvent event) {
        NounSharedPreferences.getInstance().setNounApiConfig(event.getNoun_key(), event.getNoun_secret());
    }

    public FirebaseAnalytics getAnalytics() {
        if (analytics == null) {
            analytics = FirebaseAnalytics.getInstance(this);
        }
        return analytics;
    }
}