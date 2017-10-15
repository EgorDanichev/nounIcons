package com.edanichev.nounIcons.app.main.Utils.DB.Realm;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class IconsRealmAdapter {

    Realm realm;

    public IconsRealmAdapter() {
        this.realm = Realm.getDefaultInstance();
    }

    public void addIconsToCache(String request, List<IconDetails> icons ){

        if (!checkIfExists(request)){
            realm.beginTransaction();

            Icons iconsForCache = new Icons((RealmList<IconDetails>) icons );
            iconsForCache.setRequest(request);
            realm.copyToRealm(iconsForCache);

            realm.commitTransaction();
        }
    }


    public boolean checkIfExists(String request){

        RealmResults<Icons> query = realm.where(Icons.class)
                .equalTo("request", request).findAll();

        return query.size() != 0;
    }

    public List<IconDetails>  getIconsFromCache(String request){
        RealmResults<Icons> query = realm.where(Icons.class)
                .equalTo("request", request).findAll();

        return query.get(0).getIcons();
    }


}
