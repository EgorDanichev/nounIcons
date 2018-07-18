package com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.httpUrlConnection;


import com.edanichev.nounIcons.app.main.iconlist.TranslateCallback;

public class TranslateNetwork implements TranslationResponseCallback {

    TranslateCallback translateCallback;

    public TranslateNetwork(TranslateCallback translateCallback){
        this.translateCallback = translateCallback;
    }

    public void translate (String text){
        new TranslateService(this).execute(text);
    }

    @Override
    public void onResponseRecieve (String response) {

//        try {
//            //translateCallback.onTranslateResponse(XMLParser.getString(response));
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }
    }

}