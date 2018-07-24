package com.edanichev.nounIcons.app.main.IconListPresenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Analytics.NounFirebaseAnalytics;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.InternetStatus;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException;
import com.edanichev.nounIcons.app.main.iconlist.model.SearchIconsInteractor;
import com.edanichev.nounIcons.app.main.iconlist.presenter.IconListPresenter;
import com.edanichev.nounIcons.app.main.iconlist.view.MainView;
import com.edanichev.nounIcons.app.main.iconlist.view.MainView$$State;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.mobile.android.core.domain.network.callback.RequestCallback;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public final class IconListPresenterTest extends Robolectric {

    @Mock
    private
    MainView mainView;
    @Mock
    private
    MainView$$State mainViewState;
    @Mock
    private
    SearchIconsInteractor searchIconsInteractor;
    @Mock
    private
    InternetStatus internetStatus;
    @Mock
    NounFirebaseAnalytics analytics;

    private IconListPresenter presenter;

    private List<IconDetails> iconDetailsList = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        iconDetailsList.add(new IconDetails("123", "asdasdasd", "aazzza", "asdgggcxz", "cat"));

        presenter = new IconListPresenter(
                searchIconsInteractor,
                internetStatus,
                analytics
        );
        presenter.attachView(mainView);
        presenter.setViewState(mainViewState);
    }

//    @Test
//    public void should_show_snack_if_no_internet_on_create() {
//        Mockito.when(internetStatus.isNetworkConnected()).thenReturn(false);
//
//        presenter.onCreate();
//        verify(mainViewState).showSnack(anyString());
//    }

    @Test
    public void should_show_snack_if_no_internet_on_search() {
        Mockito.when(internetStatus.isNetworkConnected()).thenReturn(false);

        presenter.getIconsList("ad");
        verify(mainViewState).showSnack(anyInt());
    }

    @Test
    public void should_show_error_if_no_empty_request() {
        presenter.getIconsList("");
        verify(mainViewState).emptyQueryError();
    }

    @Test
    public void should_show_progress_and_show_result_after_succes_response() {
        doAnswer(invocation -> {
            RequestCallback<List<IconDetails>> callback = (RequestCallback<List<IconDetails>>) invocation.getArguments()[1];
            callback.onRequestSuccess(iconDetailsList);
            return null;
        }).when(searchIconsInteractor).getIcons(anyString(), any(RequestCallback.class));

        presenter.getIconsList("cat");

        verify(mainViewState).showProgress();
        verify(mainViewState, times(1)).showIconsList(iconDetailsList);
        verify(mainViewState, times(1)).hideProgress();
    }

    @Test
    public void should_show_empty_message_when_error() {
        doAnswer(invocation -> {
            RequestCallback<List<IconDetails>> callback = (RequestCallback<List<IconDetails>>) invocation.getArguments()[1];
            callback.onRequestFailure(new EmptyListException());
            return null;
        }).when(searchIconsInteractor).getIcons(anyString(), any(RequestCallback.class));

        presenter.getIconsList("cat23eddsfasd");

        verify(mainViewState, times(1)).showProgress();
        verify(mainViewState, times(1)).onEmptyIconsList();
        verify(mainViewState, times(1)).hideProgress();
    }
}