package com.edanichev.nounIcons.app.main.NounHintCloud.Presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudView;
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences;

import javax.inject.Inject;

@InjectViewState
public class HintCloudPresenter extends MvpPresenter<HintCloudView> {

    private INounSharedPreferences preferences;
    private IHintCloudInteractor hintCloudInteractor;
    private INounConfig config;

    @Inject
    public HintCloudPresenter(INounSharedPreferences preferences, IHintCloudInteractor hintCloudInteractor, INounConfig buildConfig) {
        this.preferences = preferences;
        this.hintCloudInteractor = hintCloudInteractor;
        this.config = buildConfig;
    }

    public void onCreate() {
        loadHintCloud();
    }

    private void loadHintCloud() {
        if (!preferences.isHintSeen() || config.isDebug()) {
            getViewState().addChipsToHintCloud(hintCloudInteractor.getTags());
            preferences.setHintSeen(true);
            getViewState().showHintCloud();
        } else {
            getViewState().hideHintCloud();
        }
    }

}
