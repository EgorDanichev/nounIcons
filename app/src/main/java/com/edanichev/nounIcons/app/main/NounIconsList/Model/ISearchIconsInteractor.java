package com.edanichev.nounIcons.app.main.NounIconsList.Model;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;

public interface ISearchIconsInteractor {

    void onDestroy();

    void onCreate();

    void getIcons(String term, IconsCallback callback);
}
