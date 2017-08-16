package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsFragmentPresenter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsFragmentPresenterInterface;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Pictures.CustomLayout;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import me.gujun.android.taggroup.TagGroup;

public class IconDetailsFragmentView extends BottomSheetFragment implements IconDetailsFragmentViewInterface {

    ImageView imageView;
    ProgressBar progress;
    CustomLayout mCustomLayout;
    TagGroup tags;

    IconDetailsFragmentPresenterInterface iconDetailsFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        findView(myInflatedView);

        createPresenter();

        loadImageInBackground(getArguments().getString("iconUrl"));

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).closeBottomFragment();
            } }
        );

        iconDetailsFragmentPresenter.getIconDetails(getArguments().getString("iconId"));

        return myInflatedView;
    }

    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
    }

    public void hideProgress(){
        progress.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void showTags(List<String> tagNames){
        tags.setTags(tagNames);
    }



    private void createPresenter(){
        iconDetailsFragmentPresenter = new IconDetailsFragmentPresenter(this);
    }


    private void findView(View myInflatedView){
        imageView = (ImageView) myInflatedView.findViewById(R.id.bottom_sheet_view);
        progress = (ProgressBar) myInflatedView.findViewById(R.id.progress_bar);
        mCustomLayout = (CustomLayout) myInflatedView.findViewById(R.id.bottom_sheet_view);
        tags = (TagGroup) myInflatedView.findViewById(R.id.tag_group);

    }
    private void loadImageInBackground(String url) {

        showProgress();

        Picasso.with(getActivity()).
                load(url).
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



}