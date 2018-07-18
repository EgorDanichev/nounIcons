package com.edanichev.nounIcons.app.main.base;

import com.arellomobile.mvp.MvpView;

public interface IBaseActivityView extends MvpView {

    void showSnack(int stringId);

    void hideSnack();

    void showMessage(String message);
}