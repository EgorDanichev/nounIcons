
package com.edanichev.nounIcons.app.main.NounIconsList.Presenter;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractorImpl;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public class MainPresenterImpl implements MainPresenter, FindItemsInteractor.OnFinishedListener, IconsCallback {

    private MainView mainView;
    private FindItemsInteractor findItemsInteractor;

    public MainPresenterImpl (MainView mainView) {
        this.mainView = mainView;
        this.findItemsInteractor = new FindItemsInteractorImpl(this);
    }

    @Override public void onDestroy() {
        mainView = null;
    }


    @Override
    public void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        if (!term.equals("")) {
            mainView.showProgress();
            findItemsInteractor.getIconsList(term);

        } else {
            mainView.emptyQueryError();
        }
    }

    @Override
    public void onEmptyIconsList() {
        mainView.showMessage("No icons found!");

    }

    @Override public void onFinished(List<String> items) {
        if (mainView != null) {
            mainView.hideProgress();
        }
    }

    @Override
    public void onIconsSearchResponse(List<IconDetails> icons) {

        mainView.showIconsList(icons);
    }

}


