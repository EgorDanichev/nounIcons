package com.edanichev.nounIcons.app.main.Utils.UI.Dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth
import com.kaopiz.kprogresshud.KProgressHUD

class DialogShower {

    companion object {
        fun a(): Int = 1
        private var loadingDialog: KProgressHUD? = null

        fun showAuthDialog(context: Context) {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("You need authorization to add icon to favorites")
            dialog.setMessage("Authorize?")
            dialog.setPositiveButton("Yes") { dialog1, arg1 ->
                context.startActivity(NounFirebaseAuth.getAuthIntent())
                showLoadingDialog(context)
            }
            dialog.setNegativeButton("No") { dialog12, arg1 ->
                hideLoadingDialog()
            }
            dialog.setCancelable(true)
            dialog.show()
        }

        fun showLoadingDialog(context: Context) {
            loadingDialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        }

        fun hideLoadingDialog() {
            if (loadingDialog != null) {
                loadingDialog!!.dismiss()
            }
        }

    }

}