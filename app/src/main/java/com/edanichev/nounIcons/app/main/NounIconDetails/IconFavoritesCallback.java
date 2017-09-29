package com.edanichev.nounIcons.app.main.NounIconDetails;



public interface IconFavoritesCallback {

    void onIsIconInFavoritesResponse(boolean isFavorite);

    void onAddIconToFavorites();

    void onSuccessfulRemoveFromFavorites();

    void onFailedRemoveIconFromFavorites();

}
