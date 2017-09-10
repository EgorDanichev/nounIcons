package com.edanichev.nounIcons.app.main.Utils.DB.Realm;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Icons;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;


public class RealmMIgration implements io.realm.RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.get(Icons.class.getSimpleName())
                    .addField("request", String.class);
            oldVersion++;

        }

        if (oldVersion == 1) {
            schema.get(Icons.class.getSimpleName())
                    .addIndex("request");
            oldVersion++;

        }


    }




}
