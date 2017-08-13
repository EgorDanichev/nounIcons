
package com.edanichev.nounIcons.app.main.NounIconsList.Presenter;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.FindItemsInteractorImpl;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Icons;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class MainPresenterImpl implements MainPresenter, FindItemsInteractor.OnFinishedListener,IconsCallback {

    private MainView mainView;
    private FindItemsInteractor findItemsInteractor;

    public MainPresenterImpl (MainView mainView) {
        this.mainView = mainView;
        this.findItemsInteractor = new FindItemsInteractorImpl(this);
    }

    @Override public void onItemClicked(int position) {
        if (mainView != null) {
            mainView.showMessage(String.format("Position %d clicked", position + 1));
        }
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
        mainView.showMessage("No icons found! Idi na hui!");

    }

    @Override public void onFinished(List<String> items) {
        if (mainView != null) {
            mainView.hideProgress();
        }
    }

    public MainView getMainView() {
        return mainView;
    }


    @Override
    public void onIconsSearchResponse(Icons icons) {

        List<Icons.NounIcon> items = new ArrayList<>();

        for (Icons.NounIcon icon:icons.icons){
            items.add(icon);
        }
        mainView.showIconsList(items);
    }

}


