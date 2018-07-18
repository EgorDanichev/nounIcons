package com.edanichev.nounIcons.app.main.Utils.Analytics

import android.os.Bundle

import com.edanichev.nounIcons.app.main.NounApp
import com.google.firebase.analytics.FirebaseAnalytics

open class NounFirebaseAnalytics {

    open fun registerSearchEvent(query: String) {
        val bundle = Bundle()
        bundle.putString("searchIconsList", query)
        NounApp.app().analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    open fun registerOpenIconDetailsEvent() {
        val bundle = Bundle()
        bundle.putString("openIconDetails", "openIconDetails")
        NounApp.app().analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    open fun registerOnFavoriteButtonClick(isAuthorized: Boolean) {
        val bundle = Bundle()
        bundle.putString("onFavoriteButtonClick", "onFavoriteButtonClick")
        if (isAuthorized) {
            bundle.putString("isAuthorized", "true")
        } else {
            bundle.putString("isAuthorized", "false")
        }
        NounApp.app().analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    open fun registerAuthResultEvent(isAuthorized: Boolean) {
        val bundle = Bundle()
        bundle.putString("onAuthResult", "onAuthResult")
        if (isAuthorized) {
            bundle.putString("isAuthorized", "true")
        } else {
            bundle.putString("isAuthorized", "false")
        }
        NounApp.app().analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }

    open fun registerOnHintClick(query: String) {
        val bundle = Bundle()
        bundle.putString("onHintClick", query)
        NounApp.app().analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}
