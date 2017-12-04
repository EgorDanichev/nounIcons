package com.edanichev.nounIcons.app.main.Utils.UI.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogShower {

    private static SweetAlertDialog loadingDialog;

    public static void showAuthDialog(final Context context) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Do you want to authorize?");
        dialog.setMessage("You need authorization to add icon to favorites");
        dialog.setPositiveButton("Yes", (dialog1, arg1) -> {
            context.startActivity(NounFirebaseAuth.getAuthIntent());
            DialogShower.showLoadingDialog(context);
        });
        dialog.setNegativeButton("No", (dialog12, arg1) -> hideLoadingDialog());
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void showLoadingDialog(Context context) {
        loadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.main_color));
        loadingDialog.setTitleText("Loading");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public static void hideLoadingDialog() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

}
