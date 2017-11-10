package com.edanichev.nounIcons.app.main.Utils.UI.Pictures;


import android.content.Context;
import android.graphics.Color;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class IconLoader {

    public static IconicsDrawable getCheckedFavoriteButton(Context context) {
        return new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(Color.BLACK)
                .sizeDp(35);
    }

    public static IconicsDrawable getUncheckedFavoriteButton(Context context) {
        return new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_star_border)
                .color(Color.BLACK)
                .sizeDp(35);
    }

    public static IconicsDrawable getShareButton(Context context) {
        return new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_share)
                .color(Color.BLACK)
                .sizeDp(35);
    }
}
