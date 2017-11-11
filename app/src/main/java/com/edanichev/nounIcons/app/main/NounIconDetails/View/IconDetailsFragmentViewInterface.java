package com.edanichev.nounIcons.app.main.NounIconDetails.View;

public interface IconDetailsFragmentViewInterface {

    void showProgress();

    void hideProgress();

    void setFavoriteButtonStatus(boolean isChecked);

    void showFavoriteButton();

    void hideFavoriteButton();

    void showAuthDialog();

    void showMessageOnAdd();

    void showMessageOnRemove();

    void onSuccessAuth();

    void showLoaderDialog();

    void hideLoaderDialog();
}
