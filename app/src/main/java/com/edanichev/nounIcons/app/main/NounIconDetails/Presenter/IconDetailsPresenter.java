package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import android.support.annotation.NonNull;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconChangeFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;
import com.edanichev.nounIcons.app.main.Utils.Auth.NounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.DB.Firebase.FirebaseAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class IconDetailsPresenter implements IIconDetailsPresenter, IconChangeFavoritesCallback {

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
        FirebaseAdapter.getInstance(this).loadFavoriteStatus(icon);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onAuthDialogShow() {
        view.showLoaderDialog();
        registerAuthStateChangedListener();
        NounSharedPreferences.setAuthDialogShown(true);
    }

    public boolean isAuthorized() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
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
    public void onFailedRemoveIconFromFavorites() {
    }

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
        NounAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseAdapter.getInstance(IconDetailsPresenter.this).addIconToFavorite(icon);
            }
        });
    }

    private void removeToFavorites(final FirebaseIconDetails icon) {
        NounAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseAdapter.getInstance(IconDetailsPresenter.this).removeIconFromFavorites(icon);
            }
        });
    }

    private void registerAuthStateChangedListener() {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (NounSharedPreferences.isAuthDialogShown()) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        NounSharedPreferences.setAuthDialogShown(false);
                        view.onSuccessAuth();
                        view.hideLoaderDialog();
                    }
                }
            }
        });
    }

}



