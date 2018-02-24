package com.edanichev.nounIcons.app.main.NounIconDetails.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class IconDetails implements Parcelable{

    private String id;
    private String preview_url_84;
    private String attribution_preview_url;
    private String preview_url;
    private String term;
    private List<Tag> tags;

    protected IconDetails(Parcel in) {
        id = in.readString();
        preview_url_84 = in.readString();
        attribution_preview_url = in.readString();
        preview_url = in.readString();
        term = in.readString();
    }

    public IconDetails() {

    }

    public IconDetails(String id, String preview_url_84, String attribution_preview_url, String preview_url, String term, List<Tag> tags) {
        this.id = id;
        this.preview_url_84 = preview_url_84;
        this.attribution_preview_url = attribution_preview_url;
        this.preview_url = preview_url;
        this.term = term;
        this.tags = tags;
    }

    public IconDetails(String id, String preview_url_84, String attribution_preview_url, String preview_url, String term) {
        this.id = id;
        this.preview_url_84 = preview_url_84;
        this.attribution_preview_url = attribution_preview_url;
        this.preview_url = preview_url;
        this.term = term;
    }

    public String getId() {
        return id;
    }

    public String getPreview_url_84() {
        return preview_url_84;
    }

    public String getAttribution_preview_url() {
        return attribution_preview_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public String getTerm() {
        return term;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(preview_url_84);
        parcel.writeString(attribution_preview_url);
        parcel.writeString(preview_url);
        parcel.writeString(term);
    }

    public static final Creator<IconDetails> CREATOR = new Creator<IconDetails>() {
        @Override
        public IconDetails createFromParcel(Parcel in) {
            return new IconDetails(in);
        }

        @Override
        public IconDetails[] newArray(int size) {
            return new IconDetails[size];
        }
    };



}
