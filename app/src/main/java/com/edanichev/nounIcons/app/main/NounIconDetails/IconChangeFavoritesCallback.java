package com.edanichev.nounIcons.app.main.NounIconDetails;

public interface IconChangeFavoritesCallback {

    void onSuccessfulAddToFavorites();
    void onSuccessfulRemoveFromFavorites();

    void iconInFavoritesStatus(boolean isFavorite);
}
