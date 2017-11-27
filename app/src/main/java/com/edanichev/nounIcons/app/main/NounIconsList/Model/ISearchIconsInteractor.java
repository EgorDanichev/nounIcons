package com.edanichev.nounIcons.app.main.NounIconsList.Model;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.EmptyIconsListFromCommandEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.IconsListFromCommandEvent;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface ISearchIconsInteractor {

    void getIconsList(String term);

    void onDestroy();

    void onCreate();

    void onIconsSearchResponse(IconsListFromCommandEvent event);

    void onEmptyIconsList(EmptyIconsListFromCommandEvent event);

    void getIcons (String term, IconsCallback callback);
}
