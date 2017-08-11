package com.antonioleiva.mvpexample.app.main.NounIconsList;

import com.antonioleiva.mvpexample.app.main.Utils.Network.Microsoft.Translate.Translation;

public interface TranslateCallback {

    void onTranslateResponse(Translation response);

}
