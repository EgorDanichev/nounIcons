package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconsList.IconCallback;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail.GetIconCommand;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Pictures.CustomLayout;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class MyFragment extends BottomSheetFragment {

    ImageView imageView;
    ProgressBar progress;
    CustomLayout mCustomLayout;
    TagGroup tags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        imageView = (ImageView) myInflatedView.findViewById(R.id.bottom_sheet_view);
        progress = (ProgressBar) myInflatedView.findViewById(R.id.progress_bar);
        mCustomLayout = (CustomLayout) myInflatedView.findViewById(R.id.bottom_sheet_view);
        tags = (TagGroup) myInflatedView.findViewById(R.id.tag_group);

        String str = getArguments().getString("iconUrl");
        loadImageInBackground(str);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).closeBottomFragment();
            } }
        );

        String id = getArguments().getString("iconId");
        Presenter presenter = new Presenter(this);

        presenter.getIcon(id);


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
        progress.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    private void showTags(List<String> tagNames){

        tags.setTags(tagNames);
    }



    class Presenter implements IconCallback{

        MyFragment fragment;

        Presenter(MyFragment fragment){
            this.fragment = fragment;

        }


        public void getIcon(String id) {
            GetIconCommand iconCommand = new GetIconCommand(this);
            iconCommand.getIcon(id);
        }

        @Override
        public void onIconsSearchResponse(IconDetails icon) {

            List<String> tags = new ArrayList<>();

            for (IconDetails.tag tag: icon.icon.getTags())
                tags.add(tag.getSlug());

            showTags(tags);

        }
    }



}