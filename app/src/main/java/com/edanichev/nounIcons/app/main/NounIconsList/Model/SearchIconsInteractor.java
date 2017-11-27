package com.edanichev.nounIcons.app.main.NounIconsList.Model;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.EmptyIconsListFromCommandEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.EmptyIconsListFromInteractorEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.IconsListFromCommandEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.Model.Events.IconsListFromInteractorEvent;
import com.edanichev.nounIcons.app.main.NounIconsList.TranslateCallback;
import com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate.Translation;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class SearchIconsInteractor implements ISearchIconsInteractor, TranslateCallback {

//    TranslateCommand translateCommand = new TranslateCommand(this);

    private CompositeDisposable compositeDisposable;
    private Disposable iconsDisposable;
    Single<Icons> iconsSingle;
    private DisposableSingleObserver<Icons> disposableSingleObserver;

    public SearchIconsInteractor() {
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getIconsList(String term) {
//        GetIconsCommand.getInstance().getIcons(term);
        //translateCommand.translate(term);
    }

    @Override
    public void onDestroy() {
        unSubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onIconsSearchResponse(IconsListFromCommandEvent event) {
        if (event.icons != null)
            EventBus.getDefault().post(new IconsListFromInteractorEvent(event.icons));
        else {
            EventBus.getDefault().post(new EmptyIconsListFromInteractorEvent(true));
        }
    }

    @Subscribe
    public void onEmptyIconsList(EmptyIconsListFromCommandEvent event) {
        EventBus.getDefault().post(new EmptyIconsListFromInteractorEvent(event.isEmpty));
    }

    @Override
    public void onTranslateResponse(Translation response) {
    }

    public void getIcons(String term, final IconsCallback callback) {
        disposableSingleObserver = new DisposableSingleObserver<Icons>() {
            @Override
            public void onSuccess(Icons icons) {
                callback.onIconsSearchResponse(icons.getIcons());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof EmptyListException) {
                    callback.onEmptyIconsList();
                }
            }
        };

        if (!compositeDisposable.isDisposed()) {
            iconsSingle = GetIconsCommand.getInstance().getIconsList(term);
            iconsDisposable = iconsSingle
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(disposableSingleObserver);
            compositeDisposable.add(iconsDisposable);
        }
    }

    private void unSubscribe() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.remove(iconsDisposable);
        }
    }
}