package com.edanichev.nounIcons.app.main.IconListPresenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.ISearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.IconListPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainView$$State;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.IInternetStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class IconListPresenterTest {

    @Mock
    MainView mainView;
    @Mock
    MainView$$State mainViewState;
    @Mock
    ISearchIconsInteractor searchIconsInteractor;
    @Mock
    IInternetStatus internetStatus;

    private IconListPresenter presenter;

    private List<IconDetails> iconDetailsList = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        iconDetailsList.add(new IconDetails("123", "asdasdasd", "aazzza", "asdgggcxz", "cat"));

        presenter = new IconListPresenter(searchIconsInteractor, internetStatus);
        presenter.attachView(mainView);
        presenter.setViewState(mainViewState);
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
        doAnswer(invocation -> {
            IconsCallback callback = (IconsCallback) invocation.getArguments()[1];
            callback.onIconsSearchResponse(iconDetailsList);
            return null;
        }).when(searchIconsInteractor).getIcons(anyString(), any(IconsCallback.class));

        presenter.getIconsList("cat");
        verify(mainViewState).showProgress();
        verify(searchIconsInteractor).getIcons(anyString(), any(IconsCallback.class));
    }

    @Test
    public void should_show_empty_icon_list() {
        doAnswer(invocation -> {
            IconsCallback callback = (IconsCallback) invocation.getArguments()[1];
            callback.onEmptyIconsList();
            return null;
        }).when(searchIconsInteractor).getIcons(anyString(), any(IconsCallback.class));

        presenter.getIconsList("cat23eddsfasd");
        verify(mainViewState).showProgress();
        verify(searchIconsInteractor).getIcons(anyString(), any(IconsCallback.class));

    }
}