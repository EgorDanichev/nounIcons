package com.edanichev.nounIcons.app.main.IconListPresenter;


import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.ISearchIconsInteractor;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.SearchIconsInteractor;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IGetIconsCommand;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SearchIconsInteractorTest {

    @Mock
    IGetIconsCommand command;
    @Mock
    IconsCallback callback;

    private ISearchIconsInteractor interactor;
    private Icons icons;
    private TestSubscriber<Icons> newsModelTestSubscriber;
    private CompositeDisposable compositeDisposable;

    @Before
    public void setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(__ -> Schedulers.trampoline());

        List<IconDetails> iconDetailsList = new ArrayList<>();
        iconDetailsList.add(new IconDetails("123", "asdasdasd", "aazzza", "asdgggcxz", "cat"));
        icons = new Icons(iconDetailsList);

        newsModelTestSubscriber = new TestSubscriber<>();
        compositeDisposable = new CompositeDisposable();
    }

    @Test
    public void search_icons_successful() {
        when(command.getIconsList(anyString())).thenReturn(Single.create(e -> e.onSuccess(icons)));

        interactor = new SearchIconsInteractor(compositeDisposable, command);
        interactor.getIcons("cat", callback);
        verify(callback, times(1)).onIconsSearchResponse(anyList());
        verify(callback, never()).onEmptyIconsList();
    }

    @Test
    public void search_icons_empty() {
        when(command.getIconsList(anyString())).thenReturn(Single.error(new EmptyListException("Empty response")));

        interactor = new SearchIconsInteractor(compositeDisposable, command);
        interactor.getIcons("asdsdfadf", callback);

        verify(callback, never()).onIconsSearchResponse(anyList());
    }

}