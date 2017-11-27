package com.edanichev.nounIcons.app.main.NounIconsList.Model.Events;

public class EmptyIconsListFromCommandEvent {

    public final boolean isEmpty;

    public EmptyIconsListFromCommandEvent(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
