package com.edanichev.nounIcons.app.main.NounIconDetails;

public interface IconFavoritesCallback {

    void onSuccessfulAddToFavorites();

    void onSuccessfulRemoveFromFavorites();

    void onFailedRemoveIconFromFavorites();

    void iconInFavoritesStatus(boolean isFavorite);

}
