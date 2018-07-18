package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconChangeFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;
import com.edanichev.nounIcons.app.main.Utils.Analytics.NounFirebaseAnalytics;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.DB.Firebase.FirebaseAdapter;
import com.edanichev.nounIcons.app.main.Utils.EventBus.AuthEvent;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.NounSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

public class IconDetailsPresenter implements IIconDetailsPresenter, IconChangeFavoritesCallback {

    private IconDetailsFragmentViewInterface view;
    private boolean favorite = false;
    private NounFirebaseAnalytics analytics = new NounFirebaseAnalytics();

    public IconDetailsPresenter(IconDetailsFragmentViewInterface view) {
        analytics.registerOpenIconDetailsEvent();
        this.view = view;
    }

    @Override
    public void onFavoriteButtonClick(IconDetails icon) {
        boolean isAuthorized = NounFirebaseAuth.isAuthorized();

        if (isAuthorized) {
            analytics.registerOnFavoriteButtonClick(isAuthorized);
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
            analytics.registerOnFavoriteButtonClick(isAuthorized);
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
        registerAuthStateChangedListener();
        NounSharedPreferences.getInstance().setAuthDialogShown(true);
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
        NounAsyncTask.execute(() -> FirebaseAdapter.getInstance(IconDetailsPresenter.this).addIconToFavorite(icon));
    }

    private void removeToFavorites(final FirebaseIconDetails icon) {
        NounAsyncTask.execute(() -> FirebaseAdapter.getInstance(IconDetailsPresenter.this).removeIconFromFavorites(icon));
    }

    private void registerAuthStateChangedListener() {
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (NounSharedPreferences.getInstance().isAuthDialogShown()) {
                if (NounFirebaseAuth.isAuthorized()) {
                    NounSharedPreferences.getInstance().setAuthDialogShown(false);
                    EventBus.getDefault().post(new AuthEvent(true));
                    analytics.registerAuthResultEvent(true);
                }
            }
        });
    }
}