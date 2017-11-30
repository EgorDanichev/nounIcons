package com.edanichev.nounIcons.app.main.NounBase;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDrawer.View.DrawerView;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

public abstract class BaseActivity extends MvpAppCompatActivity implements IBaseActivityView {

    protected DrawerView drawer;
    protected Toolbar toolbar;
    protected Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        findView();
        initializeToolbar();
        initializeDagger();
        initializePresenter();
        initializeDrawer();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showSnack(String text) {
        snackbar = Snackbar.make(toolbar, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    @Override
    public void hideSnack() {
        if (snackbar != null) snackbar.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    protected abstract void initializeDagger();

    protected abstract void initializePresenter();

    protected abstract void findView();

    public abstract int getLayoutId();

    protected void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(IconLoader.getBurgerIcon());
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer();
                hideKeyboard();
            }
        });
    }

    protected void initializeDrawer() {
        drawer = new DrawerView(this);
    }

}
