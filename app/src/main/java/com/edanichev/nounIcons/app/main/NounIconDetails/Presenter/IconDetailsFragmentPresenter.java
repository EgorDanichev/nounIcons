package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;


import android.content.Context;

import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;

public class IconDetailsFragmentPresenter  {


    private Context contet;
    private IconDetailsFragmentViewInterface iconDetailsFragmentView;

    public IconDetailsFragmentPresenter(IconDetailsFragmentViewInterface fragment, Context context) {
        this.iconDetailsFragmentView = fragment;
    }


}

