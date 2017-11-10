package com.edanichev.nounIcons.app.main.NounIconDetails;

public interface IconChangeFavoritesCallback {

    void onSuccessfulAddToFavorites();
    void onSuccessfulRemoveFromFavorites();
    void onFailedRemoveIconFromFavorites();
    void iconInFavoritesStatus(boolean isFavorite);
}
