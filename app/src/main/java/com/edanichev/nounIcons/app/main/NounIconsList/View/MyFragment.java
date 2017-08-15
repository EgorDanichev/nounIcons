package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.Utils.Pictures.CustomLayout;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MyFragment extends BottomSheetFragment {

    ImageView imageView;
    ProgressBar progress;
    CustomLayout mCustomLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        imageView = (ImageView) myInflatedView.findViewById(R.id.bottom_sheet_view);
        progress = (ProgressBar) myInflatedView.findViewById(R.id.progress_bar);

        mCustomLayout = (CustomLayout) myInflatedView.findViewById(R.id.bottom_sheet_view);

        String str = getArguments().getString("text");
        loadImageInBackground(str);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).closeBottomFragment();
            } }
        );

        return myInflatedView;
    }

    public void loadImageInBackground(String str) {

        showProgress();

        Picasso.with(getActivity()).
                load(str).
                into((ImageView) mCustomLayout, new Callback(){

            @Override
            public void onSuccess() {
                hideProgress();
            }

            @Override
            public void onError() {

            }
        });

    }

    private void showProgress(){
        progress.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
    }

    private void hideProgress(){
        progress.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

}