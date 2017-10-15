package com.edanichev.nounIcons.app.main.Utils.DB.Firebase;


import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDrawer.FavoriteIconsListCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAdapter {
    private DatabaseReference mDatabase;
    private FavoriteIconsListCallback favoriteIconsListCallback;


    public FirebaseAdapter(FavoriteIconsListCallback favoriteIconsListCallback) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.favoriteIconsListCallback = favoriteIconsListCallback;
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
                        public void onCancelled(DatabaseError databaseError) { }
                    });


        }
    }

}
