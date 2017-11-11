package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentView;
import com.edanichev.nounIcons.app.main.NounIconDrawer.View.DrawerView;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenterImpl;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations;
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig;
import com.edanichev.nounIcons.app.main.Utils.UI.Dialog.DialogShower;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconShare;
import com.google.android.flexbox.FlexboxLayout;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipListener;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends MvpAppCompatActivity implements MainView, RecyclerViewAdapter.ItemClickListener, EasyPermissions.PermissionCallbacks {
    private final static int NUMBER_OF_COLUMNS = 5;

    public DrawerView drawer;

    private ProgressBar progressBar;
    private EditText searchText;
    private RecyclerView iconsGridList;
    private Button searchIconsButton;
    private Toolbar toolbar;
    private FlexboxLayout flexBox;
    private ChipCloud hintCloud;
    private ViewGroup hintLayout;
    private Snackbar snackbar;
    private RecyclerView recyclerView;
    private RelativeLayout emptyResponseLayout;
    private TextView emptyResponseText;

    IconDetailsFragmentView bottomSheetFragment;

    @InjectPresenter
    MainPresenterImpl presenter;

    private RecyclerViewAdapter iconListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(null);

        findView();
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (getWindow() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        super.onResume();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        iconsGridList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        iconsGridList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEmptyIconsList() {
        showEmptyIconsListMessage();
    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIconsList(List<IconDetails> icons) {
        if (icons != null) {
            hideProgress();
            createAdapter();
            iconListAdapter.setItems(icons);
            iconsGridList.scrollToPosition(0);
            hideEmptyIconsListMessage();
        }
    }

    @Override
    public void emptyQueryError() {
        searchText.setError(getResources().getString(R.string.blank_query_error));
    }

    public void searchIconsList(String iconsQuery) throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        searchText.setText(iconsQuery);
        presenter.getIconsList(iconsQuery.trim());
    }

    @Override
    public void onItemClick(View view, int position) {
        hideKeyboard();
        openIconDetails(iconListAdapter.iconsList.get(position));
    }

    public void openIconDetails(IconDetails icon) {
        hideKeyboard();
        IconDetailsFragmentView.openIconDetails(icon, getSupportFragmentManager());
    }

    @Override
    public void hideHintCloud() {
        hintLayout.setVisibility(View.GONE);
    }

    @Override
    public void addChipsToHintCloud(List<String> tags) {
        hintCloud.addChips(tags);
    }

    @Override
    public void showHintCloud() {
        hintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSnack(String text) {
        snackbar = Snackbar.make(iconsGridList, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    @Override
    public void hideSnack() {
        if (snackbar != null) snackbar.dismiss();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private void registerKeyboardListener() {
        KeyboardVisibilityEvent.registerEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                    }
                });
    }

    private void createAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        iconListAdapter = new RecyclerViewAdapter();
        iconListAdapter.setClickListener(this);
        recyclerView.setAdapter(iconListAdapter);
    }

    private View.OnKeyListener searchKeyListener() {
        return new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        searchIconsList(searchText.getText().toString());
                        hideKeyboard();
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        };
    }

    private TextWatcher onSearchTextChangerListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                showEmptyIconsList();
            }
        }
    };

    private void showEmptyIconsList() {
        hideProgress();
        createAdapter();
        iconListAdapter.setItems(new ArrayList<IconDetails>());
        iconsGridList.scrollToPosition(0);
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                searchIconsList(searchText.getText().toString());
                hideKeyboard();
            } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    };

    private void findView() {
        drawer = new DrawerView(this);

        progressBar = findViewById(R.id.progress);
        searchText = findViewById(R.id.search_edit);
        setSearchTextDefault();
        iconsGridList = findViewById(R.id.items_grid_list);
        searchIconsButton = findViewById(R.id.icon_list_button);
        toolbar = findViewById(R.id.toolbar);
        flexBox = findViewById(R.id.hint_cloud);
        hintLayout = findViewById(R.id.hint_layout);
        recyclerView = findViewById(R.id.items_grid_list);
        emptyResponseLayout = findViewById(R.id.empty_response_layout);
        emptyResponseText = findViewById(R.id.empty_response_text);
        hintCloud = new ChipCloud(this, flexBox, ChipConfig.getChipCloudConfig());

        searchIconsButton.setOnClickListener(buttonClickListener);
        searchText.setOnKeyListener(searchKeyListener());
        searchText.addTextChangedListener(onSearchTextChangerListener);
        hintCloud.setListener(onChipClickListener());

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

    private ChipListener onChipClickListener() {
        return new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if (b) {
                    try {
                        searchIconsList(hintCloud.getLabel(i));
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    hideHintCloud();
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 300) {
            DialogShower.hideLoadingDialog();
            if (NounFirebaseAuth.isAuthorized()) {
                showMessage("Hello " + NounFirebaseAuth.getCurrentUserName() + "!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == IconShare.RC_LOCATION_CONTACTS_PERM) {
            if (bottomSheetFragment != null)
                bottomSheetFragment.shareIconImage();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    private void showEmptyIconsListMessage() {
        progressBar.setVisibility(View.GONE);
        iconsGridList.setVisibility(View.GONE);

        String emptyText;
        emptyText = String.format(getString(R.string.we_didn_t_find_any_icons), '"' + searchText.getText().toString() + '"');
        emptyResponseText.setText(emptyText);
        emptyResponseLayout.setVisibility(View.VISIBLE);
        emptyResponseLayout.startAnimation(NounAnimations.getBecomeVisibleAnimation());
    }

    private void hideEmptyIconsListMessage() {
        if (emptyResponseLayout.getVisibility() == View.VISIBLE) {
            emptyResponseLayout.startAnimation(NounAnimations.getBecomeInvisibleAnimation());
            emptyResponseLayout.setVisibility(View.GONE);
        }
    }

    private void setSearchTextDefault() {
        searchText.setCompoundDrawables(IconLoader.getSearchIcon(), null, null, null);
        searchText.setCompoundDrawablePadding(15);
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchText.setCompoundDrawables(null, null, null, null);
            }
        });

    }

}
