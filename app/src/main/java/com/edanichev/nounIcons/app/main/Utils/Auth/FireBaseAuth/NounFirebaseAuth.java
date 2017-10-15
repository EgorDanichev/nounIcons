package com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth;

import android.app.Activity;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.Collections;

public class NounFirebaseAuth {

    public static void openAuth(Activity activity) {
        activity.startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                ))
                        .build(),100);
    }

    public static Intent getAuthIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                        Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                        ))
                .build();
    }

}
