
package com.antonioleiva.mvpexample.app.main.NounIconsList.Model;

import com.antonioleiva.mvpexample.app.main.NounIconsList.IconsCallback;
import com.antonioleiva.mvpexample.app.main.NounIconsList.TranslateCallback;
import com.antonioleiva.mvpexample.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.antonioleiva.mvpexample.app.main.Utils.Network.Noun.IconsList.Icons;
import com.antonioleiva.mvpexample.app.main.Utils.Network.Microsoft.Translate.TranslateCommand;
import com.antonioleiva.mvpexample.app.main.Utils.Network.Microsoft.Translate.Translation;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class FindItemsInteractorImpl implements FindItemsInteractor,IconsCallback,TranslateCallback {
    IconsCallback iconsCallback;

    GetIconsCommand getIconsCommand = new GetIconsCommand(this);
    TranslateCommand translateCommand = new TranslateCommand(this);


    public FindItemsInteractorImpl(IconsCallback iconsCallback){
       this.iconsCallback = iconsCallback;
    }

    @Override
    public void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        translateCommand.translate(term);
    }

    @Override
    public void onIconsSearchResponse(Icons icons) {

        if (icons!=null)
            iconsCallback.onIconsSearchResponse(icons);
        else iconsCallback.onEmptyIconsList();

    }

    @Override
    public void onEmptyIconsList() {

    }

    @Override
    public void onTranslateResponse(Translation response) {

        getIconsCommand.getIcons(response.getString());
    }
}