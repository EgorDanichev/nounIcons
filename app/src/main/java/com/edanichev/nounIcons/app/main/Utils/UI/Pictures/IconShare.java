package com.edanichev.nounIcons.app.main.Utils.UI.Pictures;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.edanichev.nounIcons.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class IconShare {

    public static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] READ_AND_WRITE =
            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public static void shareImage(ImageView imageView, Activity activity) {

        if (EasyPermissions.hasPermissions(activity, READ_AND_WRITE)) {
            Uri bitmapUri = getLocalBitmapUri(imageView, activity);
            if (bitmapUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                shareIntent.setType("image/*");
                activity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        } else {
            EasyPermissions.requestPermissions(
                    activity,
                    "You have to grant permissions to share",
                    RC_LOCATION_CONTACTS_PERM,
                    READ_AND_WRITE);
        }
    }

    private static Uri getLocalBitmapUri(ImageView imageView, Activity activity) {

        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }

        Uri bitmapUri = null;
        try {
            File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);

            Bitmap imageWithBG = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            imageWithBG.eraseColor(activity.getResources().getColor(R.color.white_color));
            Canvas canvas = new Canvas(imageWithBG);
            canvas.drawBitmap(bitmap, 0f, 0f, null);

            imageWithBG.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bitmapUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapUri;
    }


}
