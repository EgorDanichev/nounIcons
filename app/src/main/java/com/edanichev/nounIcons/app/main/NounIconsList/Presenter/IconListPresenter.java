package com.edanichev.nounIcons.app.main.NounIconsList.Presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.EmptyIconsListFromInteractorEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.IconsListFromInteractorEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.ISearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.IInternetStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class IconListPresenter extends MvpPresenter<MainView> implements IIconListPresenter, IconsCallback {

    private ISearchIconsInteractor searchIconsInteractor;
    private IInternetStatus internetStatus;

    @Inject
    public IconListPresenter(ISearchIconsInteractor searchIconsInteractor, IInternetStatus internetStatus) {
        this.searchIconsInteractor = searchIconsInteractor;
        this.internetStatus = internetStatus;
    }

    @Override
    public void onCreate() {
        searchIconsInteractor.onCreate();
    }

    @Override
    public void onDestroy() {
        if (searchIconsInteractor != null) {
            searchIconsInteractor.onDestroy();
            searchIconsInteractor = null;
        }
    }

    @Override
    public void getIconsList(String term) {
        checkInternet();
        if (!term.equals("")) {
            getViewState().showProgress();
            searchIconsInteractor.getIcons(term, this);
        } else {
            getViewState().emptyQueryError();
        }
    }

    @Override
    public void onIconsSearchResponse(List<IconDetails> icons) {
        getViewState().showIconsList(icons);
    }

    @Override
    public void onEmptyIconsList() {
        getViewState().onEmptyIconsList();
    }

    private void checkInternet() {
        if (!internetStatus.isNetworkConnected())
            getViewState().showSnack("No internet connection");
        else
            getViewState().hideSnack();
    }
}