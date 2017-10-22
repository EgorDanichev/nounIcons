package com.edanichev.nounIcons.app.main.Utils.Pictures;


import android.app.Activity;
import android.graphics.Color;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class IconLoader {

    public static IconicsDrawable getCheckedFavoriteButton(Activity activity) {
        return new IconicsDrawable(activity)
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(Color.BLACK)
                .sizeDp(30);
    }

    public static IconicsDrawable getUncheckedFavoriteButton(Activity activity) {
        return new IconicsDrawable(activity)
                .icon(GoogleMaterial.Icon.gmd_star_border)
                .color(Color.BLACK)
                .sizeDp(30);
    }

    public static IconicsDrawable getShareButton(Activity activity) {
        return new IconicsDrawable(activity)
                .icon(GoogleMaterial.Icon.gmd_share)
                .color(Color.BLACK)
                .sizeDp(30);
    }




}
