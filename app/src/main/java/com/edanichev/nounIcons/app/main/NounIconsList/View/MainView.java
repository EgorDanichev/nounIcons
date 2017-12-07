package com.edanichev.nounIcons.app.main.NounIconsList.View;

import com.arellomobile.mvp.MvpView;
import com.edanichev.nounIcons.app.main.NounBase.IBaseActivityView;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public interface MainView extends IBaseActivityView {

    void showProgress();

    void hideProgress();

    void onEmptyIconsList();

    void showIconsList(List<IconDetails> icons);

    void emptyQueryError();

}

