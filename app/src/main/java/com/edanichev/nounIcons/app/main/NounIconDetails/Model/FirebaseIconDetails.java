package com.edanichev.nounIcons.app.main.NounIconDetails.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebaseIconDetails{

    public String id;
    public String preview_url_84;
    public String attribution_preview_url;
    public String preview_url;
    public String term;

    public FirebaseIconDetails() {

    }

    public FirebaseIconDetails(String id, String preview_url_84, String attribution_preview_url, String preview_url, String term) {
        this.id = id;
        this.preview_url_84 = preview_url_84;
        this.attribution_preview_url = attribution_preview_url;
        this.preview_url = preview_url;
        this.term = term;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreview_url_84() {
        return preview_url_84;
    }

    public void setPreview_url_84(String preview_url_84) {
        this.preview_url_84 = preview_url_84;
    }

    public String getAttribution_preview_url() {
        return attribution_preview_url;
    }

    public void setAttribution_preview_url(String attribution_preview_url) {
        this.attribution_preview_url = attribution_preview_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}