package com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class NounFirebaseAuth {
//    private static String USER_PATH = "users/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static String FAVORITE_ICONS_PATH = "favoriteIcons";

    private static NounFirebaseAuth instance;
    private static DatabaseReference mDatabase;
    private static IconFavoritesCallback iconFavoritesCallback;

    private NounFirebaseAuth() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static NounFirebaseAuth getInstance(IconFavoritesCallback callback) {
        iconFavoritesCallback = callback;
        if (instance == null) {
            instance = new NounFirebaseAuth();
        }
        return instance;
    }

    public static void openAuth(Activity activity) {
        activity.startActivityForResult(getAuthIntent(),100);
    }

    public static Intent getAuthIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false,true)
                .setAvailableProviders(
                        Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                        ))
                .build();
    }

    public void addIconToFavorite(IconDetails icon) {
        FirebaseIconDetails firebaseIcon = new FirebaseIconDetails(icon.getId(),icon.getPreview_url_84(),icon.getAttribution_preview_url(),icon.getPreview_url(),icon.getTerm());
        mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FAVORITE_ICONS_PATH)
                .child(icon.getId())
                .setValue(firebaseIcon, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        iconFavoritesCallback.onSuccessfulAddToFavorites();
                    }
                });
    }


    public void removeIconFromFavorites(IconDetails icon) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FAVORITE_ICONS_PATH)
                .child(icon.getId())
                .equalTo(icon.getId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                iconFavoritesCallback.onSuccessfulRemoveFromFavorites();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void loadFavoriteStatus(IconDetails icon) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mDatabase
                    .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FAVORITE_ICONS_PATH)
                    .child(icon.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Log.d("EGOR777", "response received: true");
                                iconFavoritesCallback.iconInFavoritesStatus(true);
                            } else {
                                Log.d("EGOR777", "response received: false");
                                iconFavoritesCallback.iconInFavoritesStatus(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } else iconFavoritesCallback.iconInFavoritesStatus(false);
    }

}
