package com.edanichev.nounIcons.app.main.NounIconDetails.View;


import java.util.List;

public interface IconDetailsFragmentViewInterface {

    public void showTags(List<String> tagNames);
    public void showProgress();
    public void hideProgress();

}
