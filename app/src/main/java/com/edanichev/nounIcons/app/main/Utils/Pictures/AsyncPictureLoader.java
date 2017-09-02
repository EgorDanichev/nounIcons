package com.edanichev.nounIcons.app.main.Utils.Pictures;


import android.content.Context;
import android.os.AsyncTask;

import com.edanichev.nounIcons.app.main.NounIconDetails.PictureLoadedCallback;

public class AsyncPictureLoader extends AsyncTask<String,Void,Void> {

    PictureLoadedCallback callback;

    Context context;

    public AsyncPictureLoader(Context context, PictureLoadedCallback callback){

        this.callback = callback;
        this.context = context;

    }


    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }
}
