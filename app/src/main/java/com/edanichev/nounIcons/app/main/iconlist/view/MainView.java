package com.edanichev.nounIcons.app.main.iconlist.view;

import com.edanichev.nounIcons.app.main.base.IBaseActivityView;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;

import java.util.List;

public interface MainView extends IBaseActivityView {

    void showProgress();

    void hideProgress();

    void onEmptyIconsList();

    void showIconsList(List<IconDetails> icons);

    void emptyQueryError();
}

