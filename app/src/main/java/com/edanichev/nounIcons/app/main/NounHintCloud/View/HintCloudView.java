package com.edanichev.nounIcons.app.main.NounHintCloud.View;

import com.arellomobile.mvp.MvpView;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.CloudTagsModel;

public interface HintCloudView extends MvpView {
    void showHintCloud();

    void hideHintCloud();

    void addChipsToHintCloud(CloudTagsModel tags);
}
