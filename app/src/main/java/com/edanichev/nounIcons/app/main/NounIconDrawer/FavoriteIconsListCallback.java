package com.edanichev.nounIcons.app.main.NounIconDrawer;


import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;

import java.util.List;

public interface FavoriteIconsListCallback {

    void onFavoriteIconsListResponse(List<FirebaseIconDetails> icons);


}
