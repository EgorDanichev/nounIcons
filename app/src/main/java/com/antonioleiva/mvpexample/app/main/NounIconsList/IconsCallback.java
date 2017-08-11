package com.antonioleiva.mvpexample.app.main.NounIconsList;

import com.antonioleiva.mvpexample.app.main.Utils.Network.Noun.IconsList.Icons;

public interface IconsCallback {

    void onIconsSearchResponse(Icons icons);
    void onEmptyIconsList();
}
