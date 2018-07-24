package com.edanichev.nounIcons.app.main.IconListPresenter;


import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.edanichev.nounIcons.app.main.iconlist.model.SearchIconsInteractor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.mobile.android.core.domain.network.callback.RequestCallback;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SearchIconsInteractorTest {

    @Mock
    private
    GetIconsCommand command;
    @Mock
    private
    RequestCallback<List<IconDetails>> callback;

    private SearchIconsInteractor interactor;
    private Icons icons;

    @Before
    public void setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(__ -> Schedulers.trampoline());

        List<IconDetails> iconDetailsList = new ArrayList<>();
        iconDetailsList.add(new IconDetails("123", "asdasdasd", "aazzza", "asdgggcxz", "cat"));
        icons = new Icons(iconDetailsList);

        interactor = new SearchIconsInteractor(command, new CompositeDisposable());
    }

    @Test
    public void search_icons_successful() {
        when(command.execute())
                .thenReturn(Single.just(icons));

        interactor.getIcons("cat", callback);

        verify(callback, times(1)).onRequestSuccess(icons.getIcons());
        verify(callback, never()).onRequestFailure(any(Throwable.class));
    }

    @Test
    public void search_icons_empty() {
        when(command.execute())
                .thenReturn(Single.just(new Icons()));

        interactor.getIcons("asdsdfadf", callback);

        verify(callback, times(1)).onRequestFailure(any(EmptyListException.class));
        verify(callback, never()).onRequestSuccess(anyListOf(IconDetails.class));
    }

    @Test
    public void should_call_onfail_if_command_fails() {
        when(command.execute())
                .thenReturn(Single.error(new Throwable()));

        interactor.getIcons("asdsdfadf", callback);

        verify(callback, times(1)).onRequestFailure(any(Throwable.class));
        verify(callback, never()).onRequestSuccess(anyListOf(IconDetails.class));
    }
}