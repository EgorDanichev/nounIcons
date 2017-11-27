package com.edanichev.nounIcons.app.main.NounIconsList.View;

import com.arellomobile.mvp.MvpView;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public interface MainView extends MvpView {

    void showProgress();

    void hideProgress();

    void onEmptyIconsList();

    void showMessage(String message);

    void showIconsList(List<IconDetails> icons);

    void emptyQueryError();

    void showSnack(String text);

    void hideSnack();
}

