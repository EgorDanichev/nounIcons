package com.edanichev.nounIcons.app.main.NounIconDetails.Presenter;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentViewInterface;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class IconDetailsFragmentPresenter implements IconDetailsFragmentPresenterInterface {

    private IconDetailsFragmentViewInterface iconDetailsFragmentView;
    private IconFavoritesCallback iconFavoritesCallback;

    private DatabaseReference mDatabase;

    public IconDetailsFragmentPresenter (IconDetailsFragmentViewInterface fragment, IconFavoritesCallback iconFavoritesCallback) {
        this.iconDetailsFragmentView = fragment;
        this.iconFavoritesCallback = iconFavoritesCallback;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void addIconToFavorite(IconDetails icon) {

        FirebaseIconDetails firebaseIcon = new FirebaseIconDetails(icon.getId(),icon.getPreview_url_84(),icon.getAttribution_preview_url(),icon.getPreview_url(),icon.getTerm());

        mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favoriteIcons")
                .child(icon.getId())
                .setValue(firebaseIcon, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        iconFavoritesCallback.onAddIconToFavorites();
                    }
                });
    }

    @Override
    public void removeIconToFavorite(IconDetails icon) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favoriteIcons")
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
                iconFavoritesCallback.onFailedRemoveIconFromFavorites();
            }
        });

    }

    @Override
    public void isIconInFavorites(final IconDetails icon) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mDatabase
                    .child("users/" + user.getUid())
                    .child("favoriteIcons")
                    .child(icon.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {
                                iconFavoritesCallback.onIsIconInFavoritesResponse(true);
                            } else iconFavoritesCallback.onIsIconInFavoritesResponse(false);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
        else iconFavoritesCallback.onIsIconInFavoritesResponse(false);

    }

    public boolean isAuthorized (){
        return FirebaseAuth.getInstance().getCurrentUser() != null ;
    }

}

