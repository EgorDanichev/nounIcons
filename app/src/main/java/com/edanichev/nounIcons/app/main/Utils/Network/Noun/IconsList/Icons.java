package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Icons extends RealmObject{

    @Index
    private String request;

    private RealmList<IconDetails> icons;

    public List<IconDetails> getIcons() {
        return icons;
    }

    public void setIcons(RealmList<IconDetails> icons) {
        this.icons = icons;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Icons(RealmList<IconDetails> icons) {
        this.icons = icons;
    }

    public Icons(){

    }

    public Icons(String request, RealmList<IconDetails> icons) {
        this.request = request;
        this.icons = icons;
    }
}
