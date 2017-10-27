package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import android.os.AsyncTask;

class NimbleTask extends AsyncTask<Runnable, Void, Void> {

    @Override
    protected Void doInBackground(Runnable... runnables) {
        runnables[0].run();
        return null;
    }
}
