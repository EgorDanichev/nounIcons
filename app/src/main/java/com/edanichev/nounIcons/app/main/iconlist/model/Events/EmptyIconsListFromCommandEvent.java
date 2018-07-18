package com.edanichev.nounIcons.app.main.iconlist.model.Events;

public class EmptyIconsListFromCommandEvent {

    public final boolean isEmpty;

    public EmptyIconsListFromCommandEvent(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
