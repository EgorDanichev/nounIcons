package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsPresenter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsPresenterInterface;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Tag;
import com.edanichev.nounIcons.app.main.Utils.UI.Dialog.DialogShower;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.BottomSheetView;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconShare;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
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
import fisk.chipcloud.ChipListener;

import static android.app.Activity.RESULT_OK;

public class IconDetailsFragmentView extends BottomSheetDialogFragment implements IconDetailsFragmentViewInterface {
    private final static int AUTH_REQUEST_CODE = 100;

    private ProgressBar progress;
    private BottomSheetView bottomSheetImageView;
    private FlexboxLayout flexBox;
    private ChipCloud chipCloud;
    private TextView iconTermView;
    private ImageButton favoriteButton;
    private IconDetails iconData;
    private ImageButton shareButton;
    private Button iconButton;

    private MainActivity activity;
    public IconDetailsPresenterInterface iconDetailsPresenter;


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        iconDetailsPresenter.onDestroy();
        iconDetailsPresenter = null;
        super.onDestroy();
    }

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
        loadShareButton();

        return myInflatedView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            activity = (MainActivity) context;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        //bottomSheetImageView.setVisibility(View.INVISIBLE);
    }

    public void hideProgress(){
        progress.setVisibility(View.INVISIBLE);
        bottomSheetImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessageOnAdd() {
        activity.showMessage(activity.getResources().getString(R.string.icon_added_to_favorites));
    }

    @Override
    public void showMessageOnRemove() {
        activity.showMessage(activity.getResources().getString(R.string.icon_deleted_from_favorites));
    }

    @Override
    public void showAuthDialog() {
        DialogShower.showAuthDialog(activity,AUTH_REQUEST_CODE);
    }

    @Override
    public void setFavoriteButtonStatus(boolean isChecked) {
        if (isChecked) {
            favoriteButton.setImageDrawable(IconLoader.getCheckedFavoriteButton(activity));
        } else {
            favoriteButton.setImageDrawable(IconLoader.getUncheckedFavoriteButton(activity));
        }
        //animateButton(favoriteButton);
    }

    private void animateButton(final ImageButton button) {
        final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce);
        button.startAnimation(myAnim);
    }

    private void createPresenter() {
        iconDetailsPresenter = new IconDetailsPresenter(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                iconDetailsPresenter.onFavoriteButtonClick(iconData);
                activity.showMessage("Hello " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void findView(View myInflatedView) {
        progress = myInflatedView.findViewById(R.id.progress_bar);
        bottomSheetImageView = myInflatedView.findViewById(R.id.bottom_sheet_view);
        iconButton = myInflatedView.findViewById(R.id.icon_button);
        flexBox = myInflatedView.findViewById(R.id.flexbox_drawable);
        iconTermView =  myInflatedView.findViewById(R.id.icon_term);
        favoriteButton =  myInflatedView.findViewById(R.id.add_to_favorite_button);
        shareButton = myInflatedView.findViewById(R.id.share_button);
        chipCloud = new ChipCloud(activity, flexBox, ChipConfig.getChipCloudConfig());
        chipCloud.setListener(onChipClickListener());
        favoriteButton.setOnClickListener(onFavoriteButtonClickListener());
        shareButton.setOnClickListener(onShareButtonClickListener());
        iconButton.setOnClickListener(onIconImageClickListener());
    }

    private void receiveIconData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            iconData = bundle.getParcelable("icon");
        }
    }

    private void loadIconImage() {
        showProgress();
        String iconUrl;
        if (iconData.getAttribution_preview_url() == null)
            iconUrl = iconData.getPreview_url();
        else
            iconUrl = iconData.getAttribution_preview_url();

        Picasso.with(activity).
                load(iconUrl).
                into(bottomSheetImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgress();
                    }

                    @Override
                    public void onError() {}
                });
    }

    private void loadIconTerm() {
        iconTermView.setText(iconData.getTerm().toUpperCase());
    }

    private void showTags(){
        if (getTagsInString() != null )
            chipCloud.addChips(getTagsInString());
    }

    private void loadFavoriteButton() {
        hideFavoriteButton();
        iconDetailsPresenter.loadFavoriteStatus(iconData);
    }

    @Override
    public void hideFavoriteButton(){
        favoriteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFavoriteButton(){
        favoriteButton.setVisibility(View.VISIBLE);
    }

    private void loadShareButton() {
        shareButton.setBackground(IconLoader.getShareButton(activity));
    }

    private View.OnClickListener onFavoriteButtonClickListener() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                iconDetailsPresenter.onFavoriteButtonClick(iconData);
            }
        };
    }

    private View.OnClickListener onIconImageClickListener() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    private View.OnClickListener onShareButtonClickListener() {
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                IconShare.shareImage(bottomSheetImageView,activity);
            }
        };
    }

    private ChipListener onChipClickListener() {
        return new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if (b) {
                    try {
                        activity.searchIconsList(chipCloud.getLabel(i));
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                }
            }
        };
    }

    private List<String> getTagsInString() {
        List<String> tags = new ArrayList<>();
        if (iconData.getTags() != null) {
            for (Tag tag : iconData.getTags()) {
                if (!Objects.equals(tag.getSlug().trim(), "")) tags.add(tag.getSlug());
            }
        }
     return tags;
    }

}
