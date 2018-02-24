package com.edanichev.nounIcons.app.main.Utils.UI.Pictures;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounApp;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import static android.graphics.Bitmap.createScaledBitmap;

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

    public static Drawable getBurgerIcon(Context context) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
        Drawable icon = context.getDrawable(R.drawable.toolbar_burger);
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        return new BitmapDrawable(context.getResources(), createScaledBitmap(bitmap, px, px, true));
    }

    public static Drawable getMarkedBurgerIcon(Context context) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
        Drawable icon = context.getDrawable(R.drawable.toolbar_burger);
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        return new BitmapDrawable(context.getResources(), createScaledBitmap(bitmap, px, px, true));
    }

    public static IconicsDrawable getEmptyAccountIcon() {
        return new IconicsDrawable(NounApp.app())
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .color(Color.BLACK)
                .sizeDp(30);
    }

}
