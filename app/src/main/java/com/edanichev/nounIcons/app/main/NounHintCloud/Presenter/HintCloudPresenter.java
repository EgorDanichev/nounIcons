package com.edanichev.nounIcons.app.main.NounHintCloud.Presenter;

import com.edanichev.nounIcons.app.main.NounBase.BasePresenter;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudViewInterface;
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences;

import javax.inject.Inject;

public class HintCloudPresenter implements BasePresenter<HintCloudViewInterface>{

    private INounSharedPreferences preferences;
    private IHintCloudInteractor hintCloudInteractor;
    private INounConfig config;
    private HintCloudViewInterface view;

    @Inject
    public HintCloudPresenter(INounSharedPreferences preferences, IHintCloudInteractor hintCloudInteractor, INounConfig buildConfig) {
        this.preferences = preferences;
        this.hintCloudInteractor = hintCloudInteractor;
        this.config = buildConfig;
    }

    @Override
    public void setView(HintCloudViewInterface view) {
        this.view = view;
    }

    public void onCreate() {
        loadHintCloud();
    }

    private void loadHintCloud() {
        if (!preferences.isHintSeen() || config.isDebug()) {
            view.initView();
            view.addChipsToHintCloud(hintCloudInteractor.getTags());
            view.showHintCloud();
            preferences.setHintSeen(true);
        } else {
            view.hideHintCloud();
        }
    }
}
