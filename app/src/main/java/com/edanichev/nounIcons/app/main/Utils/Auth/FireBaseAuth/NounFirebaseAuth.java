package com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

import java.util.Collections;

public class NounFirebaseAuth {

    public static Intent getAuthIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false, true)
                .setAvailableProviders(
                        Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                        ))
                .build();
    }

}
