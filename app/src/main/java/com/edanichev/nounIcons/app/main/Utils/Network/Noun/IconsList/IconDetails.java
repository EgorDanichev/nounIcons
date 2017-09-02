package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class IconDetails extends RealmObject implements Parcelable{

    private String id;
    private String preview_url_84;
    private String attribution_preview_url;
    private String icon_url;
    private String term;
    private RealmList<Tag> tags;

    protected IconDetails(Parcel in) {
        id = in.readString();
        preview_url_84 = in.readString();
        attribution_preview_url = in.readString();
        icon_url = in.readString();
        term = in.readString();
    }

    public IconDetails() {

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

    public String getIcon_url() {
        return icon_url;
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
        parcel.writeString(icon_url);
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
