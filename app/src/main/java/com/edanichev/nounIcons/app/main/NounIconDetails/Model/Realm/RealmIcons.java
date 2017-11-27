package com.edanichev.nounIcons.app.main.NounIconDetails.Model.Realm;


import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;

public class RealmIcons extends RealmObject {

    @Index
    private String request;

    private RealmList<RealmIconDetails> icons;

    public List<RealmIconDetails> getIcons() {
        return icons;
    }

    public void setIcons(RealmList<RealmIconDetails> icons) {
        this.icons = icons;
    }

    public RealmIcons() {
    }

    public RealmIcons(String request, List<IconDetails> icons) {
        this.request = request;
        this.icons = convertToRealm(icons);
    }

    private RealmList<RealmIconDetails> convertToRealm(List<IconDetails> icons) {
        RealmList<RealmIconDetails> realmIconDetails = new RealmList<>();
        for (IconDetails icon : icons)
            realmIconDetails.add(new RealmIconDetails(icon.getId(), icon.getPreview_url_84(), icon.getAttribution_preview_url(), icon.getAttribution_preview_url(), icon.getTerm(), RealmIconDetails.convertToRealm(icon.getTags())));

        return realmIconDetails;
    }

    public static Icons convertFromRealm(RealmIcons realmIcons) {
        List<IconDetails> iconDetailsList = new ArrayList<>();

        for (RealmIconDetails realmIconDetails : realmIcons.getIcons()) {
            iconDetailsList.add(new IconDetails(
                    realmIconDetails.getId(),
                    realmIconDetails.getPreview_url_84(),
                    realmIconDetails.getAttribution_preview_url(),
                    realmIconDetails.getPreview_url(),
                    realmIconDetails.getTerm(),
                    RealmIconDetails.convertFromRealm(realmIconDetails.getTags())
            ));
        }
        return new Icons(iconDetailsList);
    }

}
