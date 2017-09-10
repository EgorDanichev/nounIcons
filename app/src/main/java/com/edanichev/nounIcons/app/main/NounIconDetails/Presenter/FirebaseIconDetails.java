package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebaseIconDetails {

    public String id;
    public String preview_url_84;
    public String attribution_preview_url;
    public String icon_url;
    public String term;

    public FirebaseIconDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(FirebaseIconDetails.class)
    }

    public FirebaseIconDetails(String id, String preview_url_84, String attribution_preview_url, String icon_url, String term) {
        this.id = id;
        this.preview_url_84 = preview_url_84;
        this.attribution_preview_url = attribution_preview_url;
        this.icon_url = icon_url;
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

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}