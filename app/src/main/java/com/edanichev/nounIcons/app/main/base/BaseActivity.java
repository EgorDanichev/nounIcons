package com.edanichev.nounIcons.app.main.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDrawer.View.DrawerView;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Toast.ToastShower;
import com.edanichev.nounIcons.app.main.Utils.UI.Dialog.DialogShower;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import kotlin.Unit;

public abstract class BaseActivity extends MvpAppCompatActivity implements IBaseActivityView {

    private KProgressHUD loadingDialog;

    public DrawerView drawer;
    public Toolbar toolbar;
    protected Snackbar snackbar;

    @Override
    protected void onResume() {
        super.onResume();
        if (getWindow() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initializeDagger();
        findView();
        initializePresenter();
        initializeDrawer();
        EventBus.getDefault().register(this);
        createProgress(this);
    }

    private void createProgress(Context context) {
        loadingDialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        loadingDialog.dismiss();
        loadingDialog = null;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        initializeToolbar();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        hideLoadingDialog();

        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showSnack(int stringId) {
        snackbar = Snackbar.make(toolbar, getString(stringId), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    @Override
    public void hideSnack() {
        if (snackbar != null) snackbar.dismiss();
    }

    @Override
    public void showMessage(String message) {
        ToastShower.showSuccessToast(message, this);
    }

    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    public void setDefaultBurger() {
        toolbar.setNavigationIcon(IconLoader.getBurgerIcon(this));
    }

    public void setMarkedBurger() {
        toolbar.setNavigationIcon(IconLoader.getMarkedBurgerIcon(this));
    }

    protected void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDefaultBurger();

        toolbar.setNavigationOnClickListener(v -> {
            drawer.openDrawer();
            hideKeyboard();
        });

    }

    public void showLoadingDialog() {
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    public void showAuthDialog() {
        DialogShower progress = new DialogShower();
        progress.setActivity(new WeakReference(this));
        new DialogShower().showAuthDialog(
                "You need authorization to add icon to favorites",
                "Authorize?",
                () -> {
                    startActivity(NounFirebaseAuth.getAuthIntent());
                    showLoadingDialog();
                    return Unit.INSTANCE;
                },
                () -> {
                    showLoadingDialog();
                    return Unit.INSTANCE;
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 300) {
            hideLoadingDialog();
            if (NounFirebaseAuth.isAuthorized()) {
                showMessage("Hello " + NounFirebaseAuth.getCurrentUserName() + "!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void initializeDrawer() {
        drawer = new DrawerView(this);
    }

    protected abstract void initializeDagger();

    protected abstract void initializePresenter();

    protected abstract void findView();

    public abstract int getLayoutId();
}