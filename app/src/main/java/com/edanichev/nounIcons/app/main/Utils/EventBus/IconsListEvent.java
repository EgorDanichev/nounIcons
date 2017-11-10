package com.edanichev.nounIcons.app.main.Utils.EventBus;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public class IconsListEvent {

    public final List<IconDetails> icons;

    public IconsListEvent(List<IconDetails> icons) {
        this.icons = icons;
    }
}