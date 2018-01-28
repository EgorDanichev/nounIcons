package com.edanichev.nounIcons.app.main.Utils.UI.Toast;

import android.app.Activity;

import com.shashank.sony.fancytoastlib.FancyToast;

public class ToastShower {

    public static void showSuccessToast(String message, Activity activity) {
        FancyToast.makeText(activity, message, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
    }

    public static void showDefaultToast(String message, Activity activity) {
        FancyToast.makeText(activity, message, FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show();
    }
}
