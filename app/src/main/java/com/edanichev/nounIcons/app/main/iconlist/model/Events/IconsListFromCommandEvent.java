package com.edanichev.nounIcons.app.main.iconlist.model.Events;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public class IconsListFromCommandEvent {

    public final List<IconDetails> icons;

    public IconsListFromCommandEvent(List<IconDetails> icons) {
        this.icons = icons;
    }
}