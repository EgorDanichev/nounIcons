package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import com.edanichev.nounIcons.app.main.Utils.EventBus.AuthEvent;

public interface IconDetailsFragmentViewInterface {

    void showProgress();

    void hideProgress();

    void setFavoriteButtonStatus(boolean isChecked);

    void showFavoriteButton();

    void hideFavoriteButton();

    void showAuthDialog();

    void showMessageOnAdd();

    void showMessageOnRemove();

    void onAuthResult(AuthEvent event);
}