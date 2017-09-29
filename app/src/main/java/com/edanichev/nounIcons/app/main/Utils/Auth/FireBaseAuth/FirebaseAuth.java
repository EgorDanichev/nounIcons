package com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth;

import android.app.Activity;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

public class FirebaseAuth {

    public static void openAuth(Activity activity){

        activity.startActivityForResult (
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                ))
                        .build(),
                100);
    }
}
