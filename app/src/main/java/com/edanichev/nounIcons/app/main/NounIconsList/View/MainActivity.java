package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.View.IconDetailsFragmentView;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenterImpl;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Icons;
import com.edanichev.nounIcons.app.main.Utils.Recycler.MyRecyclerViewAdapter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements MainView,MyRecyclerViewAdapter.ItemClickListener {


    private final static int NUMBER_OF_COLUMNS = 5;
    private static boolean isKeyboardVisible = false ;

    private ProgressBar progressBar;
    private EditText searchText;
    private RecyclerView mItemsList;
    private Button iconListButton;
    private IconDetailsFragmentView bottomSheetFragment = new IconDetailsFragmentView();

    private MainPresenter presenter;
    private MyRecyclerViewAdapter iconListAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        presenter = new MainPresenterImpl(this);
        createAdapter();
        registerKeyboardListener();

    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mItemsList.setVisibility(View.INVISIBLE);

    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        mItemsList.setVisibility(View.VISIBLE);
    }


    @Override public void showMessage(String message) {
        hideProgress();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIconsList(List<Icons.NounIcon> icons) {
        hideProgress();

        iconListAdapter.setItems(icons);
        iconListAdapter.notifyDataSetChanged();
        mItemsList.scrollToPosition(0);
    }

    @Override
    public void emptyQueryError(){
        searchText.setError( getResources().getString(R.string.blank_query_error) );
    }

    public void searchIconsList(String iconsQuery) throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        searchText.setText(iconsQuery);
        presenter.getIconsList(iconsQuery);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(View view, int position) {

        openIconDetails(position);
    }

    public void closeIconDetails(){
        if (bottomSheetFragment!= null) bottomSheetFragment.dismiss();

    }

    public void openIconDetails(int position){
        if (!isKeyboardVisible) {
            Bundle bundle = new Bundle();

            bundle.putParcelable("icon", iconListAdapter.mData.get(position));
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(getSupportFragmentManager(), R.id.bottomsheet);
        }
    }

    private void registerKeyboardListener(){
        Unregistrar unregistrar = KeyboardVisibilityEvent.registerEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        isKeyboardVisible = isOpen;
                    }
                });
    }

    private void createAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.items_grid_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        iconListAdapter = new MyRecyclerViewAdapter(this);
        iconListAdapter.setClickListener(this);
        recyclerView.setAdapter(iconListAdapter);
    }

    private View.OnKeyListener searchKeyListenet(){
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

    private void findView(){
        progressBar = (ProgressBar) findViewById(R.id.progress);
        searchText = (EditText) findViewById(R.id.search_edit);
        searchText.setOnKeyListener(searchKeyListenet());
        mItemsList = (RecyclerView) findViewById(R.id.items_grid_list);
        iconListButton = (Button) findViewById(R.id.icon_list_button);
        iconListButton.setOnClickListener(buttonClickListener());
        getSupportActionBar().hide();    }

}
