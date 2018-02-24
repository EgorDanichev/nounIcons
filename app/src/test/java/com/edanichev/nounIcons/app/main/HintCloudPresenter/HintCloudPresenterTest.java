package com.edanichev.nounIcons.app.main.HintCloudPresenter;

import com.edanichev.nounIcons.app.main.NounHintCloud.Model.CloudTagsModel;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.IHintCloudInteractor;
import com.edanichev.nounIcons.app.main.NounHintCloud.Presenter.HintCloudPresenter;
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudViewInterface;
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudView$$State;
import com.edanichev.nounIcons.app.main.Utils.BuildConfig.INounConfig;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.INounSharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public final class HintCloudPresenterTest {

    @Mock
    HintCloudViewInterface hintCloudView;
    @Mock
    HintCloudView$$State hintCloudState;
    @Mock
    INounSharedPreferences preferences;
    @Mock
    IHintCloudInteractor hintCloudInteractor;
    @Mock
    INounConfig config;

    private HintCloudPresenter presenter;

    private CloudTagsModel tags = new CloudTagsModel();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new HintCloudPresenter(preferences, hintCloudInteractor, config);
        presenter.attachView(hintCloudView);
        presenter.setViewState(hintCloudState);

        tags.add("cat");
        tags.add("info");
        tags.add("dog");
    }

    @Test
    public void should_show_hint_cloud() {
        Mockito.when(preferences.isHintSeen()).thenReturn(false);
        Mockito.when(hintCloudInteractor.getTags()).thenReturn(tags);
        Mockito.when(config.isDebug()).thenReturn(false);

        presenter.onCreate();
        verify(hintCloudState).addChipsToHintCloud(tags);
        verify(hintCloudState).showHintCloud();
    }

    @Test
    public void should_hide_hint_cloud() {
        Mockito.when(preferences.isHintSeen()).thenReturn(true);
        Mockito.when(hintCloudInteractor.getTags()).thenReturn(tags);
        Mockito.when(config.isDebug()).thenReturn(false);

        presenter.onCreate();
        verify(hintCloudState).hideHintCloud();
    }

}