package com.edanichev.nounIcons.app.main.NounIconsList;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Icons;

public interface IconsCallback {

    void onIconsSearchResponse(Icons icons);
    void onEmptyIconsList();
}
