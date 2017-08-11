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

package com.antonioleiva.mvpexample.app.main.NounIconsList.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antonioleiva.mvpexample.app.R;
import com.antonioleiva.mvpexample.app.main.NounIconsList.Presenter.MainPresenter;
import com.antonioleiva.mvpexample.app.main.NounIconsList.Presenter.MainPresenterImpl;
import com.antonioleiva.mvpexample.app.main.Utils.Recycler.ItemsAdapter;
import com.facebook.stetho.Stetho;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity implements MainView, AdapterView.OnItemClickListener {

    private ProgressBar progressBar;
    private MainPresenter presenter;
    private EditText searchText;
    private RecyclerView mItemsList;
    private ItemsAdapter mAdapter;
    private Button iconListButton;

    private void createAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mItemsList.setLayoutManager(layoutManager);
        mItemsList.setHasFixedSize(true);
        mAdapter = new ItemsAdapter();
        mItemsList.setAdapter(mAdapter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        searchText = (EditText) findViewById(R.id.search_edit);
        mItemsList = (RecyclerView) findViewById(R.id.items_List);
        iconListButton = (Button) findViewById(R.id.icon_list_button);
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


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showIconsList(List<String> icons) {
        hideProgress();
        mAdapter.foundItems = icons;
        mAdapter.notifyDataSetChanged();

    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
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

}
