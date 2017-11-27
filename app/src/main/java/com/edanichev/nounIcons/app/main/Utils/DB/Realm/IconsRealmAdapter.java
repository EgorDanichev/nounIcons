package com.edanichev.nounIcons.app.main.Utils.DB.Realm;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Realm.RealmIcons;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class IconsRealmAdapter {

    public IconsRealmAdapter() {
    }

    public void addIconsToCache(String requestUrl, List<IconDetails> icons) {
        Realm realm = Realm.getInstance(config);

        if (!checkCacheExists(requestUrl)) {
            RealmIcons iconsForCache = new RealmIcons(requestUrl, icons);
            realm.beginTransaction();
            realm.copyToRealm(iconsForCache);
            realm.commitTransaction();
            realm.close();
        }
    }

    public boolean checkCacheExists(String request) {
        Realm realm = Realm.getInstance(config);

        RealmResults<RealmIcons> query = realm.where(RealmIcons.class)
                .equalTo("request", request).findAll();
        return query.size() != 0;
    }

    public Icons getIconsFromCache(String request) {
        Realm realm = Realm.getInstance(config);

        RealmResults<RealmIcons> query = realm.where(RealmIcons.class)
                .equalTo("request", request).findAll();

        return RealmIcons.convertFromRealm(query.get(0));
    }

    private RealmConfiguration config = new RealmConfiguration.Builder()
            .schemaVersion(0)
            .migration(new RealmMigration())
            .build();

}
