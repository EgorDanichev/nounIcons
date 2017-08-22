package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Icons {
    public List<NounIcon> icons;

    public static class NounIcon implements Parcelable {
        public String id;
        public String preview_url_84;
        public String attribution_preview_url;
        public String icon_url;
        public String term;

        NounIcon(Parcel in) {
            id = in.readString();
            preview_url_84 = in.readString();
            attribution_preview_url = in.readString();
            icon_url = in.readString();
        }

        public static final Creator<NounIcon> CREATOR = new Creator<NounIcon>() {
            @Override
            public NounIcon createFromParcel(Parcel in) {
                return new NounIcon(in);
            }

            @Override
            public NounIcon[] newArray(int size) {
                return new NounIcon[size];
            }
        };

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
        }
    }
}
