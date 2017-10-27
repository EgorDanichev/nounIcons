
package com.edanichev.nounIcons.app.main.NounIconsList.Presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractorImpl;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;
import com.edanichev.nounIcons.app.main.Utils.Auth.NounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.InternetStatus;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class MainPresenterImpl extends MvpPresenter<MainView> implements MainPresenter, FindItemsInteractor.OnFinishedListener, IconsCallback {

    private FindItemsInteractor findItemsInteractor;

    public MainPresenterImpl () {
        this.findItemsInteractor = new FindItemsInteractorImpl(this);
    }

    @Override public void onDestroy(Activity activity) {
//        activity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
//        checkInternet((Activity) getViewState());
        if (!term.equals("")) {
            getViewState().showProgress();
            findItemsInteractor.getIconsList(term);
        } else {
            getViewState().emptyQueryError();
        }
    }

    @Override
    public void onCreate(Activity activity) {
        loadHintCloud();
        checkInternet(activity);
    }

    @Override
    public void onEmptyIconsList() {
        getViewState().showMessage("No icons found!");
    }

    @Override public void onFinished(List<String> items) {
        getViewState().hideProgress();
    }

    @Override
    public void onIconsSearchResponse(List<IconDetails> icons) {
            getViewState().showIconsList(icons);
    }

    private void loadHintCloud() {
        if (!NounSharedPreferences.getInstance().isHintSeen()) {
            List<String> tags = new ArrayList<>();
            tags.add("cat");
            tags.add("bread");
            getViewState().addChipsToHintCloud(tags);
            NounSharedPreferences.getInstance().setHintSeen(true);
            getViewState().showHintCloud();
        } else {
           getViewState().hideHintCloud();
        }
    }

    private void checkInternet(Activity activity) {
       // activity.registerReceiver(broadcastReceiver, new IntentFilter(InternetStatus.NETWORK_CHANGE_MESSAGE));
        if (!InternetStatus.isNetworkConnected(activity)) {
            getViewState().showSnack("No internet connection");
        } else
            getViewState().hideSnack();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getExtras().getBoolean("connected")) {
                getViewState().showSnack("No internet connection");
            } else {
                getViewState().hideSnack();
            }
        }
    };

}


