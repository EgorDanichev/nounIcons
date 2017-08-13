package com.edanichev.nounIcons.app.main.NounIconsList;

import com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate.Translation;

public interface TranslateCallback {

    void onTranslateResponse(Translation response);

}
