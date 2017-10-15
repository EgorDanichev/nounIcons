package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentView;
import com.edanichev.nounIcons.app.main.NounIconDrawer.View.DrawerView;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenterImpl;
import com.edanichev.nounIcons.app.main.Utils.Auth.NounSharedPreferences;
import com.edanichev.nounIcons.app.main.Utils.Chip.ChipConfig;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Recycler.RecyclerViewAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipListener;

public class MainActivity extends AppCompatActivity implements MainView,RecyclerViewAdapter.ItemClickListener {


    private final static int NUMBER_OF_COLUMNS = 5;
    private static boolean isKeyboardVisible = false ;

    public DrawerView drawer;

    private ProgressBar progressBar;
    private EditText searchText;
    private RecyclerView iconsGridList;
    private Button searchIconsButton;
    private IconDetailsFragmentView bottomSheetFragment;
    private Toolbar toolbar;
    private FlexboxLayout flexBox;
    private ChipCloud hintCloud;
    private ViewGroup hintLayout;

    private MainPresenter presenter;
    private RecyclerViewAdapter iconListAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        presenter = new MainPresenterImpl(this);
        createAdapter();
        registerKeyboardListener();
        loadSearchHints();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        iconsGridList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        iconsGridList.setVisibility(View.VISIBLE);
    }


    @Override public void showMessage(String message) {
        hideProgress();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIconsList(List<IconDetails> icons) {
        hideProgress();
        iconListAdapter.setItems(icons);
        iconListAdapter.notifyDataSetChanged();
        iconsGridList.scrollToPosition(0);
    }

    @Override
    public void emptyQueryError(){
        searchText.setError( getResources().getString(R.string.blank_query_error) );
    }

    public void searchIconsList(String iconsQuery) throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        searchText.setText(iconsQuery);
        presenter.getIconsList(iconsQuery.trim());
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(View view, int position) {
        openIconDetails(iconListAdapter.mData.get(position));
    }

    public void closeIconDetails(){
        if (bottomSheetFragment!= null) {
            bottomSheetFragment.dismiss();
            bottomSheetFragment = null;
        }

    }

    public void openIconDetails(IconDetails icon){
        if (!isKeyboardVisible) {
            Bundle bundle = new Bundle();

            bundle.putParcelable("icon", icon);
            bottomSheetFragment = new IconDetailsFragmentView();

            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }
    }

    private void registerKeyboardListener() {
        KeyboardVisibilityEvent.registerEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        isKeyboardVisible = isOpen;
                    }
                });
    }

    private void createAdapter() {
        RecyclerView recyclerView = findViewById(R.id.items_grid_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        iconListAdapter = new RecyclerViewAdapter(this);
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
                        hideKeyboard(v);
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        };
    }

    private TextWatcher onSearchTextChangerListener() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                 if (editable.length() == 0){
                     showIconsList(new ArrayList<IconDetails>());
                }

            }
        };

    }

    private View.OnClickListener buttonClickListener(){

        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    searchIconsList(searchText.getText().toString());
                    hideKeyboard(view);
                } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void findView() {
        drawer = new DrawerView(this);

        progressBar = findViewById(R.id.progress);
        searchText = findViewById(R.id.search_edit);
        iconsGridList = findViewById(R.id.items_grid_list);
        searchIconsButton = findViewById(R.id.icon_list_button);
        toolbar = findViewById(R.id.toolbar);
        flexBox = findViewById(R.id.hint_cloud);
        hintLayout = findViewById(R.id.hint_layout);
        hintCloud = new ChipCloud(this, flexBox, ChipConfig.getChipCloudConfig());

        searchIconsButton.setOnClickListener(buttonClickListener());
        searchText.setOnKeyListener(searchKeyListener());
        searchText.addTextChangedListener(onSearchTextChangerListener());
        hintCloud.setListener(onChipClickListener());

        searchText.setCompoundDrawables(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_search).color(Color.DKGRAY).sizeDp(20),null,null,null);
        searchText.setCompoundDrawablePadding(15);

        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchText.setCompoundDrawables(null,null,null,null);
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_view_headline).color(Color.BLACK).sizeDp(24));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer();
                hideKeyboard(v);
            }
        });

    }

    private void loadSearchHints() {
        if (!NounSharedPreferences.getInstance().isHintSeen()) {
            List<String> tags = new ArrayList<>();
            tags.add("cat");
            tags.add("bread");
            hintCloud.addChips(tags);
            NounSharedPreferences.getInstance().setHintSeen(true);
            showHintCloud();
        } else {
            hideHintCloud();
        }
    }

    private ChipListener onChipClickListener(){
        return new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {

                if (b) {
                    try {
                        searchIconsList(hintCloud.getLabel(i));
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    closeIconDetails();
                    hideHintCloud();
                }

            }
        };
    }

    private void hideHintCloud(){
        hintLayout.setVisibility(View.GONE);
    }

    private void showHintCloud() {
        hintLayout.setVisibility(View.VISIBLE);
    }

}
