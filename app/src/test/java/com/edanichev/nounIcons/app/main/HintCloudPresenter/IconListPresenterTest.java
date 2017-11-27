package com.edanichev.nounIcons.app.main.HintCloudPresenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.EmptyIconsListFromInteractorEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.IconsListFromInteractorEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.SearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.IconListPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView$$State;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.IInternetStatus;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public final class IconListPresenterTest {

    @Mock
    MainView mainView;
    @Mock
    MainView$$State mainViewState;
    @Mock
    SearchIconsInteractor searchIconsInteractor;
    @Mock
    IInternetStatus internetStatus;
    @Mock
    EventBus eventBus;

    private IconListPresenter presenter;

    private List<IconDetails> iconDetailsList = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new IconListPresenter(searchIconsInteractor, internetStatus);
        presenter.attachView(mainView);
        presenter.setViewState(mainViewState);

        iconDetailsList.add(new IconDetails("123", "asdasdasd", "aazzza", "asdgggcxz", "cat"));
    }

    @Test
    public void should_show_snack_if_no_internet_on_create() {
        Mockito.when(internetStatus.isNetworkConnected()).thenReturn(false);

        presenter.onCreate();
        verify(mainViewState).showSnack(anyString());
    }

    @Test
    public void should_show_snack_if_no_internet_on_search() {
        Mockito.when(internetStatus.isNetworkConnected()).thenReturn(false);

        presenter.getIconsList("ad");
        verify(mainViewState).showSnack(anyString());
    }

    @Test
    public void should_show_error_if_no_empty_request() {
        presenter.getIconsList("");
        verify(mainViewState).emptyQueryError();
    }

    @Test
    public void should_show_progress_and_make_search() {

        presenter.getIconsList("cat");
        verify(mainViewState).showProgress();
        verify(searchIconsInteractor).getIconsList("cat");

        presenter.onIconsSearchResponse(new IconsListFromInteractorEvent(iconDetailsList));
        verify(mainViewState).showIconsList(iconDetailsList);
    }

    @Test
    public void should_show_empty_icon_list() {

        presenter.getIconsList("cat23eddsfasd");
        verify(mainViewState).showProgress();
        verify(searchIconsInteractor).getIconsList("cat23eddsfasd");

        presenter.onEmptyIconsList(new EmptyIconsListFromInteractorEvent(true));
        verify(mainViewState).onEmptyIconsList();
    }
}