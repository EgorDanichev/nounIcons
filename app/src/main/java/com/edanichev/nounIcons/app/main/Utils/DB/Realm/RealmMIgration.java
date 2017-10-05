package com.edanichev.nounIcons.app.main.Utils.DB.Realm;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IconDetails;
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

        if (oldVersion == 4) {

            if (!schema.get(IconDetails.class.getSimpleName()).hasField("preview_url")) {
                schema.get(IconDetails.class.getSimpleName())
                        .addField("preview_url", String.class);
            }

            if (schema.get(IconDetails.class.getSimpleName()).hasField("icon_url")) {
                schema.get(IconDetails.class.getSimpleName())
                        .removeField("icon_url");
            }

            oldVersion++;

        }


    }




}
