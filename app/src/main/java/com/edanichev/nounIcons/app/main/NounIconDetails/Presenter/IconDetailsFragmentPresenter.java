package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;


import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;
import com.edanichev.nounIcons.app.main.NounIconsList.IconDetailsCallback;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail.GetIconDetailsCommand;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail.IconDetails;

import java.util.ArrayList;
import java.util.List;

public class IconDetailsFragmentPresenter implements IconDetailsFragmentPresenterInterface, IconDetailsCallback {



    private IconDetailsFragmentViewInterface iconDetailsFragmentView;

    public IconDetailsFragmentPresenter(IconDetailsFragmentViewInterface fragment) {
        this.iconDetailsFragmentView = fragment;
    }

    @Override
    public void getIconDetails(String id) {
        GetIconDetailsCommand iconCommand = new GetIconDetailsCommand(this);
        iconCommand.getIcon(id);
    }


    @Override
    public void onIconsSearchResponse(IconDetails icon) {
        List<String> tags = new ArrayList<>();

        for (IconDetails.tag tag : icon.icon.getTags()) {
            tags.add(tag.getSlug());
            iconDetailsFragmentView.showTags(tags);
        }
    }
}

