package com.edanichev.nounIcons.app.main.NounBase;

import com.arellomobile.mvp.MvpView;

public interface IBaseActivityView extends MvpView {
    void showSnack(String text);

    void hideSnack();

    void showMessage(String message);
}
