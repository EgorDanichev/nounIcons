package com.edanichev.nounIcons.app.main.NounIconDrawer;


import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.FirebaseIconDetails;

import java.util.List;

public interface FavoriteIconsListCallback {

    void onFavoriteIconsListResponse(List<FirebaseIconDetails> icons);


}
