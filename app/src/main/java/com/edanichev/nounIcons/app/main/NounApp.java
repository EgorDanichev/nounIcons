package com.edanichev.nounIcons.app.main;

import android.app.Application;

import com.edanichev.nounIcons.app.main.Utils.Auth.TokenStorage;
import com.edanichev.nounIcons.app.main.Utils.DB.Realm.RealmMIgration;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class NounApp extends Application {

    public Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();

        TokenStorage.initInstance(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(3)
                .migration(new RealmMIgration())
                .build();

        realm = Realm.getInstance(config);
        Realm.setDefaultConfiguration(config);

        realm.close();

    }


}
