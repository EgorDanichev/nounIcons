package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounApp;
import com.edanichev.nounIcons.app.main.NounBase.BaseActivity;
import com.edanichev.nounIcons.app.main.NounHintCloud.Model.CloudTagsModel;
import com.edanichev.nounIcons.app.main.NounHintCloud.Presenter.HintCloudPresenter;
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudView;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentView;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.IconListPresenter;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.NounIconListService;
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations;
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig;
import com.edanichev.nounIcons.app.main.Utils.UI.Dialog.DialogShower;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconShare;
import com.google.android.flexbox.FlexboxLayout;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipListener;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements MainView, RecyclerViewAdapter.ItemClickListener, EasyPermissions.PermissionCallbacks, HintCloudView {
    private final static int NUMBER_OF_COLUMNS = 4;

    private ProgressBar progressBar;
    private EditText searchText;
    private RecyclerView iconsGridList;
    private Button searchIconsButton;
    private FlexboxLayout flexBox;
    private ChipCloud hintCloud;
    private ViewGroup hintLayout;
    private ViewGroup emptyResponseLayout;
    private TextView emptyResponseText;

    private IconDetailsFragmentView bottomSheetFragment;

    @InjectPresenter
    @Inject
    IconListPresenter iconListPresenter;

    @ProvidePresenter
    IconListPresenter provideIconListPresenter() {
        return iconListPresenter;
    }

    @InjectPresenter
    @Inject
    HintCloudPresenter hintCloudPresenter;

    @ProvidePresenter
    HintCloudPresenter provideHintCloudPresenter() {
        return hintCloudPresenter;
    }

    private RecyclerViewAdapter iconListAdapter;

    @Override
    protected void initializeDagger() {
        NounApp.app().getComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        iconListPresenter.attachView(this);
        iconListPresenter.onCreate();

        hintCloudPresenter.attachView(this);
        hintCloudPresenter.onCreate();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        iconListPresenter.onDestroy();
        iconListPresenter.detachView(this);
        hintCloudPresenter.onDestroy();
        hintCloudPresenter.detachView(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (searchText.length() > 0) {
            searchIconsList(searchText.getText().toString());
        }

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

    public void searchIconsList(String iconsQuery) {
        searchText.setText(iconsQuery);
        iconListPresenter.getIconsList(iconsQuery.trim());
    }

    @Override
    public void onItemClick(View view, int position) {
        hideKeyboard();
        openIconDetails(iconListAdapter.iconsList.get(position));
    }

    public void openIconDetails(IconDetails icon) {
        hideKeyboard();
        bottomSheetFragment = IconDetailsFragmentView.openIconDetails(icon, getSupportFragmentManager());
    }

    @Override
    public void hideHintCloud() {
        hintLayout.setVisibility(View.GONE);
    }

    @Override
    public void addChipsToHintCloud(CloudTagsModel tags) {
        hintCloud.addChips(tags);
    }

    @Override
    public void showHintCloud() {
        hintLayout.setVisibility(View.VISIBLE);
    }

    private void createAdapter() {
        iconsGridList.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        iconListAdapter = new RecyclerViewAdapter();
        iconListAdapter.setClickListener(this);
        iconsGridList.setAdapter(iconListAdapter);
    }

    private View.OnKeyListener searchKeyListener() {
        return new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean isEnterKeyPressed = event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER;

                if (isEnterKeyPressed) {
                    searchIconsList(searchText.getText().toString());
                    hideKeyboard();
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
        iconListAdapter.setItems(new ArrayList<>());
        iconsGridList.scrollToPosition(0);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchIconsList(searchText.getText().toString());
            hideKeyboard();
        }
    };

    @Override
    protected void findView() {
        getWindow().setBackgroundDrawable(null);

        progressBar = findViewById(R.id.progress);
        searchText = findViewById(R.id.search_edit);
        setSearchTextDefault();
        iconsGridList = findViewById(R.id.items_grid_list);
        searchIconsButton = findViewById(R.id.icon_list_button);
        flexBox = findViewById(R.id.hint_cloud);
        hintLayout = findViewById(R.id.hint_layout);
        emptyResponseLayout = findViewById(R.id.empty_response_layout);
        emptyResponseText = findViewById(R.id.empty_response_text);
        hintCloud = new ChipCloud(this, flexBox, ChipConfig.getChipCloudConfig());

        searchIconsButton.setOnClickListener(buttonClickListener);
        searchText.setOnKeyListener(searchKeyListener());
        searchText.addTextChangedListener(onSearchTextChangerListener);
        hintCloud.setListener(onChipClickListener());
    }

    private ChipListener onChipClickListener() {
        return new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if (b) {
                    searchIconsList(hintCloud.getLabel(i));
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
                StringBuilder sb = new StringBuilder();
                sb.append("Hello ").append(NounFirebaseAuth.getCurrentUserName()).append("!");
                showMessage(sb.toString());
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

        String emptyText = String.format(getString(R.string.we_didn_t_find_any_icons), '"' + searchText.getText().toString() + '"');
        emptyResponseText.setText(emptyText);
        emptyResponseLayout.setVisibility(View.VISIBLE);
        emptyResponseLayout.startAnimation(NounAnimations.getBecomeVisibleAnimation(NounAnimations.LONG_FADE));
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
        searchText.setOnFocusChangeListener((view, b) -> searchText.setCompoundDrawables(null, null, null, null));
    }

}
