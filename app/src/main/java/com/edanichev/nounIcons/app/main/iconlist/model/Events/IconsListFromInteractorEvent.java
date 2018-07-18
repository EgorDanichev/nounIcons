package com.edanichev.nounIcons.app.main.iconlist.model.Events;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public class IconsListFromInteractorEvent {

    public final List<IconDetails> icons;

    public IconsListFromInteractorEvent(List<IconDetails> icons) {
        this.icons = icons;
    }
}