package com.edanichev.nounIcons.app.main.iconlist;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public interface IconsCallback {

    void onIconsSearchResponse(List<IconDetails> icons);

    void onEmptyIconsList();
}