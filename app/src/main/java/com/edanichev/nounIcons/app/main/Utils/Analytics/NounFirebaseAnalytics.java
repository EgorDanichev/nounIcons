package com.edanichev.nounIcons.app.main.Utils.Analytics;

import android.os.Bundle;

import com.edanichev.nounIcons.app.main.NounApp;
import com.google.firebase.analytics.FirebaseAnalytics;

public class NounFirebaseAnalytics {

    public static void registerSearchEvent(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("searchIconsList", query);
        NounApp.app().getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void registerOpenIconDetailsEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("openIconDetails", "openIconDetails");
        NounApp.app().getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void registerOnFavoriteButtonClick(boolean isAuthorized) {
        Bundle bundle = new Bundle();
        bundle.putString("onFavoriteButtonClick", "onFavoriteButtonClick");
        if (isAuthorized) {
            bundle.putString("isAuthorized", "true");
        } else {
            bundle.putString("isAuthorized", "false");
        }
        NounApp.app().getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void registerAuthResultEvent(boolean isAuthorized) {
        Bundle bundle = new Bundle();
        bundle.putString("onAuthResult", "onAuthResult");
        if (isAuthorized) {
            bundle.putString("isAuthorized", "true");
        } else {
            bundle.putString("isAuthorized", "false");
        }
        NounApp.app().getAnalytics().logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    public static void registerOnHintClick(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("onHintClick", query);
        NounApp.app().getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
