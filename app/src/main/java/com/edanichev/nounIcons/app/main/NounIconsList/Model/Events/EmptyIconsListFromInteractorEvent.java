package com.edanichev.nounIcons.app.main.NounIconsList.Model.Events;

public class EmptyIconsListFromInteractorEvent {

    public final boolean isEmpty;

    public EmptyIconsListFromInteractorEvent(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
