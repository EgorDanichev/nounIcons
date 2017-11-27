package com.edanichev.nounIcons.app.main.NounIconDetails.Model.Realm;

import android.os.Parcel;
import android.os.Parcelable;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Tag;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmIconDetails extends RealmObject implements Parcelable {

    private String id;
    private String preview_url_84;
    private String attribution_preview_url;
    private String preview_url;
    private String term;
    private RealmList<RealmTag> tags;

    protected RealmIconDetails(Parcel in) {
        id = in.readString();
        preview_url_84 = in.readString();
        attribution_preview_url = in.readString();
        preview_url = in.readString();
        term = in.readString();
    }

    public RealmIconDetails() {

    }

    public RealmIconDetails(String id, String preview_url_84, String attribution_preview_url, String preview_url, String term, RealmList<RealmTag> tags) {
        this.id = id;
        this.preview_url_84 = preview_url_84;
        this.attribution_preview_url = attribution_preview_url;
        this.preview_url = preview_url;
        this.term = term;
        this.tags = tags;
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

    public RealmList<RealmTag> getTags() {
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

    public static final Creator<RealmIconDetails> CREATOR = new Creator<RealmIconDetails>() {
        @Override
        public RealmIconDetails createFromParcel(Parcel in) {
            return new RealmIconDetails(in);
        }

        @Override
        public RealmIconDetails[] newArray(int size) {
            return new RealmIconDetails[size];
        }
    };

    public static RealmList<RealmTag> convertToRealm(List<Tag> tags) {
        RealmList<RealmTag> realmTags = new RealmList<>();
        for (Tag tag : tags)
            realmTags.add(new RealmTag(tag.getId(), tag.getSlug()));

        return realmTags;
    }

    public static List<Tag> convertFromRealm(RealmList<RealmTag> realmTags) {
        List<Tag> tags = new ArrayList<>();

        for (RealmTag realmTag : realmTags) {
            tags.add(new Tag(realmTag.getId(), realmTag.getSlug()));
        }
        return tags;
    }

}
