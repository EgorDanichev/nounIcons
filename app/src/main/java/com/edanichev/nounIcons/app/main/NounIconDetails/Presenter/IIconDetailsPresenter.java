package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

public interface IIconDetailsPresenter {

    void onFavoriteButtonClick(IconDetails icon);
    void loadFavoriteStatus(IconDetails icon);
    void onDestroy();
    void onAuthDialogShow();
}
