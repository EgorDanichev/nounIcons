package com.edanichev.nounIcons.app.main.NounIconDetails;



public interface IconFavoritesCallback {

    void onIsIconInFavoritesResponse(boolean isFavorite);

    void onAddIconToFavorites();

    void onSuccessfulRemoveIconFromFavorites();

    void onFailedRemoveIconFromFavorites();

}
