package com.edanichev.nounIcons.app.main.Utils.UI.Dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;

public class DialogShower {

    public static void showAuthDialog(final Activity activity, final int authRequestCode) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Do you want to authorize?");
        dialog.setMessage("You need authorization to add icon to favorites");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                activity.startActivityForResult(NounFirebaseAuth.getAuthIntent(), authRequestCode);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        dialog.setCancelable(true);
        dialog.show();
    }
}
