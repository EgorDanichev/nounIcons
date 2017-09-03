package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsFragmentPresenterInterface;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Tag;
import com.edanichev.nounIcons.app.main.Utils.Pictures.BottomSheetView;
import com.google.android.flexbox.FlexboxLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class IconDetailsFragmentView extends BottomSheetDialogFragment implements IconDetailsFragmentViewInterface {

    private ImageView iconImageView;
    private ProgressBar progress;
    private BottomSheetView bottomSheetView;
    private FlexboxLayout flexbox;
    private ChipCloud chipCloud;
    private TextView iconTermView;
    private Button favoriteButton;

    private IconDetails iconData;
    private boolean favorite = false;

    public IconDetailsFragmentPresenterInterface iconDetailsFragmentPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        findView(myInflatedView);

        createPresenter();
        receiveIconData();
        loadImageInBackground();
        loadIconTerm();
        showTags();

        return myInflatedView;
    }



    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        iconImageView.setVisibility(View.INVISIBLE);
    }

    public void hideProgress(){
        progress.setVisibility(View.INVISIBLE);
        iconImageView.setVisibility(View.VISIBLE);
    }

    private void showTags(){
        chipCloud.addChips(getTagsInString());
    }


    private void createPresenter(){
    }

    private void findView(View myInflatedView){

        iconImageView = (ImageView) myInflatedView.findViewById(R.id.bottom_sheet_view);
        iconImageView.setOnClickListener(imageClickListener());
        progress = (ProgressBar) myInflatedView.findViewById(R.id.progress_bar);
        bottomSheetView = (BottomSheetView) myInflatedView.findViewById(R.id.bottom_sheet_view);
        flexbox = (FlexboxLayout) myInflatedView.findViewById(R.id.flexbox_drawable);
        iconTermView = (TextView) myInflatedView.findViewById(R.id.icon_term);
        favoriteButton = (Button) myInflatedView.findViewById(R.id.add_to_favorite_button); 

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)
                .checkedChipColor(Color.parseColor("#FF9800"))
                .checkedTextColor(Color.parseColor("#000000"))
                .uncheckedChipColor(Color.parseColor("#000000"))
                .uncheckedTextColor(Color.parseColor("#FF9800"))
                .useInsetPadding(true);

        chipCloud = new ChipCloud(getActivity(), flexbox,config);
        chipCloud.setListener(chipListener());
        
        favoriteButton.setOnClickListener(favoriteButtonListener());

    }
    
    
    private View.OnClickListener favoriteButtonListener(){
        
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                if (!favorite) {

                    IconicsDrawable icon = new IconicsDrawable(getActivity())
                            .icon(GoogleMaterial.Icon.gmd_star)
                            .color(Color.BLACK)
                            .sizeDp(30);
                    view.setBackground(icon);

                    favorite = true;
                    favoriteButton.startAnimation(myAnim);
                }
                else {
                    IconicsDrawable icon =new IconicsDrawable(getActivity())
                            .icon(GoogleMaterial.Icon.gmd_star_border)
                            .color(Color.BLACK)
                            .sizeDp(30);
                    view.setBackground(icon);

                    favorite = false;
                    favoriteButton.startAnimation(myAnim);
                }

            }
        };
    }

    private void receiveIconData(){

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            iconData = bundle.getParcelable("icon");
        }
    }

    private void loadImageInBackground() {

        showProgress();
        Picasso.with(getActivity()).
                load(iconData.getAttribution_preview_url()).
                into((ImageView) bottomSheetView, new Callback(){

                    @Override
                    public void onSuccess() {
                        hideProgress();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void loadIconTerm(){
        iconTermView.setText(iconData.getTerm().toUpperCase());
    }

    private View.OnClickListener imageClickListener(){

        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).closeIconDetails();
            }
        };
    }

    private ChipListener chipListener(){

        return new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {

                if (b) {
                    try {
                        ((MainActivity) getActivity()).searchIconsList(chipCloud.getLabel(i));
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    ((MainActivity) getActivity()).closeIconDetails();
                }

            }
        };
    }

    private List<String> getTagsInString(){
        List<String> tags = new ArrayList<>();

        for(Tag tag:iconData.getTags()) {
            if (!Objects.equals(tag.getSlug().trim(), "")) tags.add(tag.getSlug());
        }
     return tags;
    }


}