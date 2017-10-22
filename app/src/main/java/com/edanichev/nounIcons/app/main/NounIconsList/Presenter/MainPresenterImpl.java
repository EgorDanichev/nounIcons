
package com.edanichev.nounIcons.app.main.NounIconsList.Presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractorImpl;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

@InjectViewState
public class MainPresenterImpl extends MvpPresenter<MainView> implements MainPresenter, FindItemsInteractor.OnFinishedListener, IconsCallback {

    private FindItemsInteractor findItemsInteractor;

    public MainPresenterImpl () {
        this.findItemsInteractor = new FindItemsInteractorImpl(this);
    }

    @Override public void onDestroy() {}

    @Override
    public void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Log.d("EGOR666",getViewState() + "presenter.getIconsList");
        if (!term.equals("")) {
            getViewState().showProgress();
            findItemsInteractor.getIconsList(term);
        } else {
            getViewState().emptyQueryError();
        }
    }

    @Override
    public void onEmptyIconsList() {
        getViewState().showMessage("No icons found!");

    }

    @Override public void onFinished(List<String> items) {
        if (getViewState() != null) {
            getViewState().hideProgress();
        }
    }

    @Override
    public void onIconsSearchResponse(List<IconDetails> icons) {
        Log.d("EGOR666","Presenter.onIconsResponse: " + icons.size());
        if (getViewState() != null) {
            getViewState().showIconsList(icons);
        }
    }

}


