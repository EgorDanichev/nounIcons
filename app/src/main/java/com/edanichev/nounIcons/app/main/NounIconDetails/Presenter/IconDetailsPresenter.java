package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import android.util.Log;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class IconDetailsPresenter implements IconDetailsPresenterInterface, IconFavoritesCallback {

    private IconDetailsFragmentViewInterface view;
    private boolean favorite = false;

    public IconDetailsPresenter(IconDetailsFragmentViewInterface view) {
        this.view = view;
        Log.d("_EGOR777","in presenter constructor: " + this.view);
        Log.d("_EGOR777","in presenter constructor: " + this);

    }

    @Override
    public void onFavoriteButtonClick(IconDetails icon) {
        if (isAuthorized()) {
            if (!favorite) {
                NounFirebaseAuth.getInstance(this).addIconToFavorite(icon);
                view.setFavoriteButtonStatus(true);
                favorite = true;
            } else {
                NounFirebaseAuth.getInstance(this).removeIconFromFavorites(icon);
                view.setFavoriteButtonStatus(false);
                favorite = false;
            }
        } else {
            view.showAuthDialog();
        }
    }

    @Override
    public void loadFavoriteStatus(final IconDetails icon) {
        Log.d("_EGOR777","loading fav status from presenter: " + this);
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
        view.showMessageOnAdd();
    }

    @Override
    public void onSuccessfulRemoveFromFavorites() {
        view.showMessageOnRemove();
    }

    @Override
    public void onFailedRemoveIconFromFavorites() {}

    @Override
    public void iconInFavoritesStatus(boolean isFavorite) {
        Log.d("_EGOR777","status received: " + this + "  "+ isFavorite);
        Log.d("_EGOR777","status received: " + view + "  "+ isFavorite);
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

