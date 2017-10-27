package com.edanichev.nounIcons.app.main.NounIconDrawer.View;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.FirebaseIconDetails;
import com.edanichev.nounIcons.app.main.NounIconDrawer.FavoriteIconsListCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.DB.Firebase.FirebaseAdapter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class DrawerView implements FavoriteIconsListCallback {

    private static long FAVORITE_ITEM_ID = 1;
    private static long SIGN_OUT_ITEM_ID = 2;
    private static long PROFILE_ITEM_WITH_PIC_ID = 123;
    private static long PROFILE_ITEM_WITHOUT_PIC_ID = 123;
    private static long DEFAULT_PROFILE_ITEM_ID = 123;

    private MainActivity activity;
    private AccountHeader accountHeader;
    private Drawer drawer;
    private FirebaseAdapter firebaseAdapter;
    private IDrawerItem signOutItem = null;
    
    private List<FirebaseIconDetails> favoriteIcons;

    public DrawerView (Context context){
        this.activity = (MainActivity) context;
        firebaseAdapter = new FirebaseAdapter(this);
        firebaseAdapter.loadFavoriteIcons();
        createDrawer();
        refreshDrawerItems();
        onAuthStateChangedListener();
    }

    public void openDrawer() {
        drawer.openDrawer();
    }

    @Override
    public void onFavoriteIconsListResponse(List<FirebaseIconDetails> icons) {
        Collections.sort(icons);
        favoriteIcons = icons;

        refreshDrawerItems();
        refreshFavoriteItems(favoriteIcons);
    }

    private void refreshDrawerItems() {
        drawer.removeAllItems();
        refreshSignOut();
        addFavoriteItem();
    }

    private void createDrawer() {
        accountHeader = populateProfile();
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar((Toolbar) activity.findViewById(R.id.toolbar))
                .withAccountHeader(accountHeader)
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(onDrawerItemClick())
                .build();
    }

    private void  addFavoriteItem() {
        drawer.addItem(new ExpandableDrawerItem()
                .withIdentifier(FAVORITE_ITEM_ID)
                .withSelectable(false)
                .withSubItems()
                .withName("My favorites")
                .withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_star).color(Color.BLACK).sizeDp(30)));
    }

    private void refreshFavoriteItems(List<FirebaseIconDetails> icons) {
        for (FirebaseIconDetails icon : icons){

            UrlPrimaryDrawerItem newItem = new UrlPrimaryDrawerItem();
            newItem.withName(icon.getTerm())
                    .withLevel(2)
                    .withIdentifier(Integer.parseInt(icon.getId()))
                    .withSelectable(false);

            newItem.withIcon(icon.getPreview_url_84());
            drawer.getDrawerItem(FAVORITE_ITEM_ID).getSubItems().add(newItem);
        }
        drawer.getAdapter().notifyAdapterDataSetChanged();
    }

    private void addSignOutItem() {
        signOutItem = new SecondaryDrawerItem()
                .withIdentifier(SIGN_OUT_ITEM_ID)
                .withSelectable(false)
                .withName("Sign Out")
                .withIcon( new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_exit_to_app).color(Color.BLACK).sizeDp(30) );

        drawer.addStickyFooterItem(signOutItem);
    }

    private void removeSignOutItem() {
        signOutItem = null;
        drawer.removeStickyFooterItemAtPosition(0);
    }

    private AccountHeader populateProfile() {
        ProfileDrawerItem headerProfile;
        drawerImageLoader();
        headerProfile = currentProfile();

        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.main_color)
                .withCurrentProfileHiddenInList(true)
                .addProfiles(headerProfile)
                .withTextColor(Color.BLACK)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderSelectionViewClickListener(onAccountClickListener())
                .build();
    }

    private void refreshProfile() {
        accountHeader.updateProfile(currentProfile());
    }

    private void refreshSignOut() {
        if (drawer != null) {
            FirebaseUser user = currentUser();
            if (user != null ){
                if (signOutItem == null) { addSignOutItem();}
            } else if (signOutItem != null)
                removeSignOutItem();
        }
    }

    private ProfileDrawerItem currentProfile() {
        FirebaseUser user = currentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                return new ProfileDrawerItem()
                        .withName(user.getDisplayName())
                        .withIcon(user.getPhotoUrl())
                        .withEmail(user.getEmail())
                        .withIdentifier(PROFILE_ITEM_WITH_PIC_ID);
            } else {
                return new ProfileDrawerItem()
                        .withName(user.getDisplayName())
                        .withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_account_circle).color(Color.BLACK).sizeDp(30))
                        .withEmail(user.getEmail())
                        .withIdentifier(PROFILE_ITEM_WITHOUT_PIC_ID);
            }
        } else {
            return new ProfileDrawerItem()
                    .withName("Tap to login")
                    .withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_account_circle).color(Color.BLACK).sizeDp(30))
                    .withIdentifier(DEFAULT_PROFILE_ITEM_ID);
        }
    }

    private FirebaseUser currentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private AccountHeader.OnAccountHeaderSelectionViewClickListener onAccountClickListener() {
        return new AccountHeader.OnAccountHeaderSelectionViewClickListener() {

            @Override
            public boolean onClick(View view, IProfile profile) {
                NounFirebaseAuth.openAuth(activity);
                return true;
            }
        }     ;
    }

    private void drawerImageLoader() {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {

            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(activity.getResources().getDrawable(R.drawable.transparent)).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });
    }

    private Drawer.OnDrawerItemClickListener onDrawerItemClick() {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if (drawerItem.getIdentifier() == SIGN_OUT_ITEM_ID) {
                    AuthUI.getInstance()
                            .signOut(activity)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        refreshSignOut();
                                        refreshProfile();
                                        activity.showMessage("Bye Bye!");
                                    }
                        }
                    });

                }

                if (isFavoriteItem(drawerItem.getIdentifier())) {

                    IconDetails clickedIcon = getFavoriteIconById(drawerItem.getIdentifier()) ;
                    if (clickedIcon != null)
                    activity.openIconDetails(clickedIcon);
                }
                return true;
            }
        };
    }

    private void onAuthStateChangedListener() {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                refreshProfile();
                refreshDrawerItems();
                firebaseAdapter.loadFavoriteIcons();
            }
        });
    }

    private boolean isFavoriteItem(long itemId) {
        boolean result = false;

        if (favoriteIcons != null) {
            for (FirebaseIconDetails icon : favoriteIcons) {
                if (icon.getId().equals(Long.toString(itemId)))
                    return true;
            }
        }

        return result;
    }

    private IconDetails getFavoriteIconById(long id) {
        IconDetails result = null;
        for (FirebaseIconDetails icon :favoriteIcons) {
            if (icon.getId().equals(Long.toString(id))) {
                result = new IconDetails(icon.getId(),icon.getPreview_url_84(),icon.getAttribution_preview_url(),icon.getPreview_url(),icon.getTerm());
            }
        }
        return result;
    }

}
