package com.edanichev.nounIcons.app.main.NounIconDetails.Model.Realm;

import io.realm.RealmObject;

public class RealmTag extends RealmObject {
    String id;
    String slug;

    public RealmTag() {}

    public RealmTag(String id, String slug) {
        this.id = id;
        this.slug = slug;
    }

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
