package com.edanichev.nounIcons.app.main.Utils.UI.Pictures;


import android.graphics.Color;

import com.edanichev.nounIcons.app.main.NounApp;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class IconLoader {

    public static IconicsDrawable getCheckedFavoriteButton() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(Color.BLACK)
                .sizeDp(35);
    }

    public static IconicsDrawable getUncheckedFavoriteButton() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_star_border)
                .color(Color.BLACK)
                .sizeDp(35);
    }

    public static IconicsDrawable getShareButton() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_share)
                .color(Color.BLACK)
                .sizeDp(35);
    }

    public static IconicsDrawable getSearchIcon() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.DKGRAY)
                .sizeDp(20);
    }

    public static IconicsDrawable getBurgerIcon() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_view_headline)
                .color(Color.BLACK)
                .sizeDp(24);
    }

    public static IconicsDrawable getEmptyAccountIcon() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .color(Color.BLACK)
                .sizeDp(30);
    }

}
