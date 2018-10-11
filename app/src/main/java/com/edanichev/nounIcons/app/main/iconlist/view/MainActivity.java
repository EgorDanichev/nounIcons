package com.edanichev.nounIcons.app.main.iconlist.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounApp;
import com.edanichev.nounIcons.app.main.NounHintCloud.Presenter.HintCloudPresenter;
import com.edanichev.nounIcons.app.main.NounHintCloud.View.HintCloudView;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentView;
import com.edanichev.nounIcons.app.main.Utils.EventBus.ChipClickEvent;
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconShare;
import com.edanichev.nounIcons.app.main.base.BaseActivity;
import com.edanichev.nounIcons.app.main.iconlist.presenter.IconListPresenter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements MainView, RecyclerViewAdapter.ItemClickListener, EasyPermissions.PermissionCallbacks {
    private final static int NUMBER_OF_COLUMNS_FOR_PORTRAIT = 4;
    private final static int NUMBER_OF_COLUMNS_FOR_LANDSCAPE = 6;

    private ProgressBar progressBar;
    private EditText searchText;
    private RecyclerView iconsGridList;
    private Button searchIconsButton;
    private ViewGroup emptyResponseLayout;
    private TextView emptyResponseText;

    private RecyclerViewAdapter iconListAdapter;
    private IconDetailsFragmentView bottomSheetFragment;

    @InjectPresenter
    @Inject
    IconListPresenter iconListPresenter;

    @ProvidePresenter
    IconListPresenter provideIconListPresenter() {
        return iconListPresenter;
    }

    @Inject
    HintCloudPresenter hintCloudPresenter;

    private HintCloudView hintCloudView;

    @Override
    protected void initializeDagger() {
        NounApp.app().getComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAdapter();
    }

    @Override
    protected void initializePresenter() {
        iconListPresenter.attachView(this);

        hintCloudPresenter.setView(hintCloudView);
        hintCloudView.setPresenter(hintCloudPresenter);
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
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchText.length() > 0) {
            searchIconsList(searchText.getText().toString());
        }
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

    @Subscribe
    public void searchIconList(ChipClickEvent event) {
        searchIconsList(event.getText());
    }

    private void createAdapter() {
        int numberOfColumns = NUMBER_OF_COLUMNS_FOR_PORTRAIT;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numberOfColumns = NUMBER_OF_COLUMNS_FOR_LANDSCAPE;
        }
        iconsGridList.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        iconListAdapter = new RecyclerViewAdapter();
        iconListAdapter.setClickListener(this);
        iconsGridList.setAdapter(iconListAdapter);
    }

    private View.OnKeyListener searchKeyListener() {
        return (v, keyCode, event) -> {
            boolean isEnterKeyPressed = event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER;
            if (isEnterKeyPressed) {
                searchIconsList(searchText.getText().toString());
                hideKeyboard();
                return true;
            }
            return false;
        };
    }

    private TextWatcher onSearchTextChangerListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                showEmptyIconsList();
            }
        }
    };

    private void showEmptyIconsList() {
        hideProgress();
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

        hintCloudView = findViewById(R.id.hint_cloud);

        progressBar = findViewById(R.id.progress);
        searchText = findViewById(R.id.search_edit);
        setSearchTextDefault();
        iconsGridList = findViewById(R.id.items_grid_list);
        searchIconsButton = findViewById(R.id.icon_list_button);
        emptyResponseLayout = findViewById(R.id.empty_response_layout);
        emptyResponseText = findViewById(R.id.empty_response_text);

        searchIconsButton.setOnClickListener(buttonClickListener);
        searchText.setOnKeyListener(searchKeyListener());
        searchText.addTextChangedListener(onSearchTextChangerListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == IconShare.RC_LOCATION_CONTACTS_PERM) {
            if (bottomSheetFragment != null)
                bottomSheetFragment.shareIconImage();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {}

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
        searchText.setOnFocusChangeListener((view, b) -> searchText.setCompoundDrawables(null, null, null, null));
    }
}