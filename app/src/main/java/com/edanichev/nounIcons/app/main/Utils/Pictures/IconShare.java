package com.edanichev.nounIcons.app.main.Utils.Pictures;


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

public class IconShare {


    public static void shareImage(ImageView imageView, Activity activity) {

        Uri bmpUri = getLocalBitmapUri(imageView,activity);



        if (bmpUri != null) {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");


            activity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {

        }
    }


    private static Uri getLocalBitmapUri (ImageView imageView,Activity activity) {

        Drawable drawable = imageView.getDrawable();

        Bitmap bmp = null;

        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }

        Uri bmpUri = null;
        try {

            File file =  new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);

            Bitmap imageWithBG = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),bmp.getConfig());
            imageWithBG.eraseColor(activity.getResources().getColor(R.color.shared_icon_color));
            Canvas canvas = new Canvas(imageWithBG);
            canvas.drawBitmap(bmp, 0f, 0f, null);

            imageWithBG.compress (Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }



}
