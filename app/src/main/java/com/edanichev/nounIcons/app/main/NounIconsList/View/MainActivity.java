/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.Presenter.MainPresenterImpl;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Icons;
import com.edanichev.nounIcons.app.main.Utils.Recycler.MyRecyclerViewAdapter;
import com.facebook.stetho.Stetho;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements MainView,MyRecyclerViewAdapter.ItemClickListener {

    private ProgressBar progressBar;
    private EditText searchText;
    private RecyclerView mItemsList;
    private Button iconListButton;
    BottomSheetLayout bottomSheet;
    MyFragment bottomSheetFragment = new MyFragment();

    private MainPresenter presenter;
    private MyRecyclerViewAdapter adapter;

    private final static int NUMBER_OF_COLUMNS = 5;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        searchText = (EditText) findViewById(R.id.search_edit);
        mItemsList = (RecyclerView) findViewById(R.id.items_grid_list);
        iconListButton = (Button) findViewById(R.id.icon_list_button);
        bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);

        createAdapter();

        presenter = new MainPresenterImpl(this);

        searchText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        searchIconsList(v);
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });


        Stetho.initializeWithDefaults(this);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mItemsList.setVisibility(View.GONE);

    }

    @Override public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        mItemsList.setVisibility(View.VISIBLE);
    }


    @Override public void showMessage(String message) {
        hideProgress();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIconsList(List<Icons.NounIcon> icons) {
        hideProgress();

        adapter.setItems(icons);
        adapter.notifyDataSetChanged();
    }

    public void searchIconsList(View view) throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        String iconsQuery = searchText.getText().toString();
        hideKeyboard(view);
        presenter.getIconsList(iconsQuery);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void emptyQueryError(){
        searchText.setError( getResources().getString(R.string.blank_query_error) );
    }

    @Override
    public void onItemClick(View view, int position) {

        Bundle bundle = new Bundle();
        bundle.putString("text", adapter.getItemUrl(position));

        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(getSupportFragmentManager(), R.id.bottomsheet);

    }

    private void createAdapter() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.items_grid_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        adapter = new MyRecyclerViewAdapter(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    public void closeBottomFragment(){
        bottomSheetFragment.dismiss();
    }

}
