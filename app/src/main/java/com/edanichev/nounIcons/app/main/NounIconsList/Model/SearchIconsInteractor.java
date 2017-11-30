package com.edanichev.nounIcons.app.main.NounIconsList.Model;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.TranslateCallback;
import com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate.Translation;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IGetIconsCommand;

import javax.inject.Inject;

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
    private IGetIconsCommand getIconsCommand;
    private Single<Icons> iconsSingle;

    private DisposableSingleObserver<Icons> disposableSingleObserver;

    @Inject
    public SearchIconsInteractor(CompositeDisposable compositeDisposable, IGetIconsCommand getIconsCommand) {
        this.compositeDisposable = compositeDisposable;
        this.getIconsCommand = getIconsCommand;
    }

    @Override
    public void onDestroy() {
        unSubscribe();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onTranslateResponse(Translation response) {
    }

    @Override
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
            iconsSingle = getIconsCommand.getIconsList(term);
            iconsDisposable = iconsSingle
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(disposableSingleObserver);
            compositeDisposable.add(iconsDisposable);
        }
    }

    private void unSubscribe() {
        if (iconsDisposable != null) {
            if (!compositeDisposable.isDisposed()) {
                compositeDisposable.remove(iconsDisposable);
            }
        }
    }
}