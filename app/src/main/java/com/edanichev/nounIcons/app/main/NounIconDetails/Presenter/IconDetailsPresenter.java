package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;

public class IconDetailsPresenter implements IconDetailsPresenterInterface, IconFavoritesCallback {

    private IconDetailsFragmentViewInterface view;
    private boolean favorite = false;

    public IconDetailsPresenter(IconDetailsFragmentViewInterface view) {
        this.view = view;
    }

    @Override
    public void onFavoriteButtonClick(IconDetails icon) {
        if (isAuthorized()) {
            if (!favorite) {
                addToFavorites(FirebaseIconDetails.converter(icon));
                view.setFavoriteButtonStatus(true);
                favorite = true;
            } else {
                removeToFavorites(FirebaseIconDetails.converter(icon));
                view.setFavoriteButtonStatus(false);
                favorite = false;
            }
        } else {
            view.showAuthDialog();
        }
    }

    @Override
    public void loadFavoriteStatus(final IconDetails icon) {
        NounFirebaseAuth.getInstance(this).loadFavoriteStatus(icon);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    public boolean isAuthorized (){
        return FirebaseAuth.getInstance().getCurrentUser() != null ;
    }

    @Override
    public void onSuccessfulAddToFavorites() {
        if (view != null)
        view.showMessageOnAdd();
    }

    @Override
    public void onSuccessfulRemoveFromFavorites() {
        if (view != null) view.showMessageOnRemove();
    }

    @Override
    public void onFailedRemoveIconFromFavorites() {}

    @Override
    public void iconInFavoritesStatus(boolean isFavorite) {
        if (view != null) {
            if (isFavorite) {
                view.setFavoriteButtonStatus(true);
                view.showFavoriteButton();
                favorite = true;
            } else {
                view.setFavoriteButtonStatus(false);
                view.showFavoriteButton();
                favorite = false;
            }
        }
    }

    private void addToFavorites(final FirebaseIconDetails icon) {
        NimbleTask.execute(new Runnable() {
            @Override
            public void run() {
                NounFirebaseAuth.getInstance(IconDetailsPresenter.this).addIconToFavorite(icon);
            }
        });
    }

    private void removeToFavorites(final FirebaseIconDetails icon) {
        NimbleTask.execute(new Runnable() {
            @Override
            public void run() {
                NounFirebaseAuth.getInstance(IconDetailsPresenter.this).removeIconFromFavorites(icon);
            }
        });
    }

}



