package com.edanichev.nounIcons.app.main.NounIconsList;

import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail.IconDetails;

public interface IconDetailsCallback {
    void onIconsSearchResponse(IconDetails iconDetails);
}
