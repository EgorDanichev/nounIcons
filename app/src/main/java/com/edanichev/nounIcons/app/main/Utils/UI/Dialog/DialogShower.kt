package com.edanichev.nounIcons.app.main.Utils.UI.Dialog

import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import com.edanichev.nounIcons.app.main.base.BaseActivity
import com.kaopiz.kprogresshud.KProgressHUD
import java.lang.ref.WeakReference

class DialogShower {

    private var loadingDialog: KProgressHUD? = null
    var activity: WeakReference<BaseActivity>? = null


    fun showProgress() {
        getActivity {
            loadingDialog = KProgressHUD.create(it)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        }
    }

    fun hideProgress() {
        loadingDialog?.dismiss()
    }

    fun showAuthDialog(
        dialogTitle: String,
        dialogMessage: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit) {

        getActivity {
            AlertDialog.Builder(it)
                .setMessage(dialogMessage)
                .setTitle(dialogTitle)
                .setPositiveButton("Yes") { dialog1, arg1 ->
                    positiveAction.invoke()
                }
                .setNegativeButton("No") { dialog12, arg1 ->
                    negativeAction.invoke()
                }
                .setCancelable(true)
                .show()
        }
    }

    private fun getActivity(action: (activity: FragmentActivity) -> Unit) {
        activity?.get()?.let { action(it) }
    }
}