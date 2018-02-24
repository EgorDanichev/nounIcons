package com.edanichev.nounIcons.app.main.Utils.DB.Firebase;


import com.edanichev.nounIcons.app.main.NounIconDetails.IconChangeFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDrawer.FavoriteIconsListCallback;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.EventBus.NounApiConfigEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAdapter {
    private static String USER_PATH = "users/";
    private static String FAVORITE_ICONS_PATH = "favoriteIcons";
    private static String CONFIG_PATH = "config/";
    private static String KEY_PATH = "noun_key";
    private static String SECRET_PATH = "noun_secret";

    private static FirebaseAdapter instance;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FavoriteIconsListCallback favoriteIconsListCallback;
    private IconChangeFavoritesCallback iconChangeFavoritesCallback;

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

    public void loadFavoriteIcons() {
        if (NounFirebaseAuth.isAuthorized()) {
            mDatabase.child(USER_PATH + NounFirebaseAuth.getCurrentUser().getUid())
                    .child(FAVORITE_ICONS_PATH)
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
        mDatabase.child(USER_PATH + NounFirebaseAuth.getCurrentUser().getUid())
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
        Query query = mDatabase.child(USER_PATH + NounFirebaseAuth.getCurrentUser().getUid())
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
        if (NounFirebaseAuth.isAuthorized()) {
            mDatabase.child(USER_PATH + NounFirebaseAuth.getCurrentUser().getUid())
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

    public static void getConfigKey() {
        mDatabase.child(CONFIG_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.child(KEY_PATH).getValue().toString();
                        String secret = dataSnapshot.child(SECRET_PATH).getValue().toString();
                        EventBus.getDefault().post(new NounApiConfigEvent(key, secret));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}
