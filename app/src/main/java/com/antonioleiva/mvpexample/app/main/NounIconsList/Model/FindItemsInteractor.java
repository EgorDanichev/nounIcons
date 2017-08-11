package com.antonioleiva.mvpexample.app.main.NounIconsList.Model;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public interface FindItemsInteractor {

    interface OnFinishedListener {
        void onFinished(List<String> items);
    }
    void getIconsList(String term) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

}
