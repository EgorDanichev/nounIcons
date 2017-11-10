package com.edanichev.nounIcons.app.main.Utils.DB.Firebase;


import com.edanichev.nounIcons.app.main.NounIconDetails.IconChangeFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDrawer.FavoriteIconsListCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAdapter {
    private static String USER_PATH = "users/";
    private static String FAVORITE_ICONS_PATH = "favoriteIcons";

    private static FirebaseAdapter instance;
    private DatabaseReference mDatabase;
    private FavoriteIconsListCallback favoriteIconsListCallback;
    private IconChangeFavoritesCallback iconChangeFavoritesCallback;

    private FirebaseAdapter() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAdapter getInstance(IconChangeFavoritesCallback callback) {
        if (instance == null) {
            instance = new FirebaseAdapter();
        }
        instance.iconChangeFavoritesCallback = callback;
        return instance;
    }


    public static FirebaseAdapter getInstance(FavoriteIconsListCallback favoriteIconsListCallback) {
        if (instance == null) {
            instance = new FirebaseAdapter();
        }
        instance.favoriteIconsListCallback = favoriteIconsListCallback;
        return instance;
    }

    public static FirebaseAdapter getInstance() {
        if (instance == null) {
            instance = new FirebaseAdapter();
        }
        return instance;
    }


    public void loadFavoriteIcons() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            mDatabase
                    .child("users/" + user.getUid())
                    .child("favoriteIcons")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final List<FirebaseIconDetails> favorites = new ArrayList<>();
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                            for (DataSnapshot child : children) {
                                favorites.add(child.getValue(FirebaseIconDetails.class));
                            }
                            favoriteIconsListCallback.onFavoriteIconsListResponse(favorites);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
    }

    public void addIconToFavorite(FirebaseIconDetails icon) {
        mDatabase.child(USER_PATH + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FAVORITE_ICONS_PATH)
                .child(icon.getId())
                .setValue(icon, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        iconChangeFavoritesCallback.onSuccessfulAddToFavorites();
                    }
                });
    }

    public void removeIconFromFavorites(FirebaseIconDetails icon) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child(USER_PATH + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FAVORITE_ICONS_PATH)
                .child(icon.getId())
                .equalTo(icon.getId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                iconChangeFavoritesCallback.onSuccessfulRemoveFromFavorites();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void loadFavoriteStatus(IconDetails icon) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mDatabase
                    .child(USER_PATH + FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FAVORITE_ICONS_PATH)
                    .child(icon.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                iconChangeFavoritesCallback.iconInFavoritesStatus(true);
                            } else {
                                iconChangeFavoritesCallback.iconInFavoritesStatus(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } else iconChangeFavoritesCallback.iconInFavoritesStatus(false);
    }


}
