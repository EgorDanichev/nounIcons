package com.edanichev.nounIcons.app.main.NounIconsList.Model;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.TranslateCallback;
import com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate.Translation;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public class FindItemsInteractorImpl implements FindItemsInteractor,IconsCallback,TranslateCallback {
    IconsCallback iconsCallback;
//    TranslateCommand translateCommand = new TranslateCommand(this);

    public FindItemsInteractorImpl(IconsCallback iconsCallback) {
       this.iconsCallback = iconsCallback;
    }

    @Override
    public void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        GetIconsCommand.getInstance(this).getIcons(term);
        //translateCommand.translate(term);
    }

    @Override
    public void onIconsSearchResponse(List<IconDetails> icons) {
        if (icons!=null)
            iconsCallback.onIconsSearchResponse(icons);
        else
            iconsCallback.onEmptyIconsList();
    }

    @Override
    public void onEmptyIconsList() {
        iconsCallback.onEmptyIconsList();

    }

    @Override
    public void onTranslateResponse(Translation response) {
    }
}