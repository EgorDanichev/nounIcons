package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.IconFavoritesCallback;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsFragmentPresenter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsFragmentPresenterInterface;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.Tag;
import com.edanichev.nounIcons.app.main.Utils.Pictures.BottomSheetView;
import com.edanichev.nounIcons.app.main.Utils.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.Pictures.IconShare;
import com.google.android.flexbox.FlexboxLayout;
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

public class IconDetailsFragmentView extends BottomSheetDialogFragment implements IconDetailsFragmentViewInterface, IconFavoritesCallback {

    private ProgressBar progress;
    private BottomSheetView bottomSheetImageView;
    private FlexboxLayout flexBox;
    private ChipCloud chipCloud;
    private TextView iconTermView;
    private Button favoriteButton;
    private IconDetails iconData;
    private Button shareButton;

    private boolean favorite = false;

    public IconDetailsFragmentPresenterInterface iconDetailsFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        findView(myInflatedView);

        createPresenter();
        receiveIconData();
        loadIconImage();
        loadIconTerm();
        showTags();
        loadFavoriteButton();

        return myInflatedView;
    }

    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        bottomSheetImageView.setVisibility(View.INVISIBLE);
    }

    public void hideProgress(){
        progress.setVisibility(View.INVISIBLE);
        bottomSheetImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onIsIconInFavoritesResponse(boolean isFavorite) {

        favorite = isFavorite;
        if (isFavorite) {
            setFavoriteButtonStatus(true);
            showFavoriteButton();

        } else {
            showFavoriteButton();
            setFavoriteButtonStatus(false);
        }

    }

    @Override
    public void onAddIconToFavorites() {
        ((MainActivity) getActivity()).showMessage(getActivity().getResources().getString(R.string.icon_added_to_favorites));
    }

    @Override
    public void onSuccessfulRemoveFromFavorites() {
        ((MainActivity) getActivity()).showMessage(getActivity().getResources().getString(R.string.icon_deleted_from_favorites));
    }

    @Override
    public void onFailedRemoveIconFromFavorites() {
        ((MainActivity) getActivity()).showMessage(getActivity().getResources().getString(R.string.icon_deleted_from_favorites_fail));
    }

    private void findView(View myInflatedView) {

        progress = myInflatedView.findViewById(R.id.progress_bar);
        bottomSheetImageView = myInflatedView.findViewById(R.id.bottom_sheet_view);
        bottomSheetImageView.setOnClickListener(onIconImageClickListener());
        flexBox = myInflatedView.findViewById(R.id.flexbox_drawable);
        iconTermView =  myInflatedView.findViewById(R.id.icon_term);
        favoriteButton =  myInflatedView.findViewById(R.id.add_to_favorite_button);
        shareButton = myInflatedView.findViewById(R.id.share_button);

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)
                .checkedChipColor(Color.parseColor("#FF9800"))
                .checkedTextColor(Color.parseColor("#000000"))
                .uncheckedChipColor(Color.parseColor("#000000"))
                .uncheckedTextColor(Color.parseColor("#FF9800"))
                .useInsetPadding(true);

        chipCloud = new ChipCloud(getActivity(), flexBox,config);
        chipCloud.setListener(onChipClickListener());

        favoriteButton.setOnClickListener(onFavoriteButtonClickListener());

    }

    private void createPresenter(){
        iconDetailsFragmentPresenter = new IconDetailsFragmentPresenter(this,this);

    }

    private void receiveIconData(){

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            iconData = bundle.getParcelable("icon");
        }
    }

    private void loadIconImage() {

        showProgress();

        String iconUrl = null;

        if (iconData.getAttribution_preview_url() == null)
            iconUrl = iconData.getPreview_url();
        else
            iconUrl = iconData.getAttribution_preview_url();

        Picasso.with(getActivity()).
                load(iconUrl).
                into(bottomSheetImageView, new Callback(){

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

    private void showTags(){
        if (getTagsInString() != null )
            chipCloud.addChips(getTagsInString());
    }

    private void loadFavoriteButton(){
        hideFavoriteButton();
        iconDetailsFragmentPresenter.isIconInFavorites(iconData);
    }

    private void hideFavoriteButton(){
        favoriteButton.setVisibility(View.INVISIBLE);
    }

    private void showFavoriteButton(){
        favoriteButton.setVisibility(View.VISIBLE);
    }


    private View.OnClickListener onFavoriteButtonClickListener(){

        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if (iconDetailsFragmentPresenter.isAuthorized()) {

                    if (!favorite) {
                        iconDetailsFragmentPresenter.addIconToFavorite(iconData);
                        setFavoriteButtonStatus(true);
                        favorite = true;
                    } else {
                        setFavoriteButtonStatus(false);
                        favorite = false;
                        iconDetailsFragmentPresenter.removeIconToFavorite(iconData);
                    }
                } else {
                    AlertDialog.Builder ad;
                    ad = new AlertDialog.Builder(getContext());
                    ad.setTitle("You need authorization to add to favorites");
                    ad.setMessage("Do you want to authorize?");
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.FirebaseAuth.openAuth(getActivity());
                        }
                    });
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {

                        }
                    });
                    ad.setCancelable(true);

                    ad.show();

                }
            }
        };
    }

    private View.OnClickListener onIconImageClickListener(){

        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
               // shareImage();

                IconShare.shareImage(bottomSheetImageView,getActivity());

                ((MainActivity)getActivity()).closeIconDetails();
            }
        };
    }

    private ChipListener onChipClickListener(){

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

//    private void shareImage() {
//
//        BottomSheetView ivImage = bottomSheetImageView;
//
//        Uri bmpUri = getLocalBitmapUri(ivImage);
//        if (bmpUri != null) {
//
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//            shareIntent.setType("image/*");
//
//            startActivity(Intent.createChooser(shareIntent, "Share Image"));
//        } else {
//
//        }
//    }

    private List<String> getTagsInString(){
        List<String> tags = new ArrayList<>();

        if (iconData.getTags() != null) {
            for (Tag tag : iconData.getTags()) {
                if (!Objects.equals(tag.getSlug().trim(), "")) tags.add(tag.getSlug());
            }
        }
     return tags;
    }

    private void setFavoriteButtonStatus(boolean isChecked){

        final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        if (isChecked) {
            favoriteButton.setBackground(IconLoader.getCheckedFavoriteButton(getActivity()));
        } else {
            favoriteButton.setBackground(IconLoader.getUncheckedFavoriteButton(getActivity()));
        }

        favoriteButton.startAnimation(myAnim);
    }




}
