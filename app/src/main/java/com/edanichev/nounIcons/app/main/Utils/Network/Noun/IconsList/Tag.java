package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import io.realm.RealmObject;

public class Tag extends RealmObject {
    String id;
    String slug;

    public String getId() {
        return id;
    }
    public String getSlug() {
        return slug;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
