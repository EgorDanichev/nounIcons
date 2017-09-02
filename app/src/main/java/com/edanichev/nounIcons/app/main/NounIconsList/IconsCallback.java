package com.edanichev.nounIcons.app.main.NounIconsList;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IconDetails;
import java.util.List;

public interface IconsCallback {

    void onIconsSearchResponse(List<IconDetails> icons);
    void onEmptyIconsList();
}
