package com.edanichev.nounIcons.app.main.NounIconsList.Model;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.TranslateCallback;
import com.edanichev.nounIcons.app.main.Utils.EventBus.EmptyIconsListEvent;
import com.edanichev.nounIcons.app.main.Utils.EventBus.IconsListEvent;
import com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate.Translation;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


public class FindIconsInteractorImpl implements FindIconsInteractor, TranslateCallback {

    IconsCallback iconsCallback;
//    TranslateCommand translateCommand = new TranslateCommand(this);

    public FindIconsInteractorImpl(IconsCallback iconsCallback) {
        this.iconsCallback = iconsCallback;
    }

    @Override
    public void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        GetIconsCommand.getInstance().getIcons(term);
        //translateCommand.translate(term);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onIconsSearchResponse(IconsListEvent event) {
        if (event.icons != null)
            iconsCallback.onIconsSearchResponse(event.icons);
        else
            iconsCallback.onEmptyIconsList();
    }

    @Subscribe
    public void onEmptyIconsList(EmptyIconsListEvent event) {
        if (event.isEmpty)
            iconsCallback.onEmptyIconsList();
    }

    @Override
    public void onTranslateResponse(Translation response) {
    }
}