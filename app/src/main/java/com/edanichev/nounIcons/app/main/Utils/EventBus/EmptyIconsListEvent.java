package com.edanichev.nounIcons.app.main.Utils.EventBus;

public class EmptyIconsListEvent {

    public final boolean isEmpty;

    public EmptyIconsListEvent(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
