package com.edanichev.nounIcons.app.main.NounIconDetails.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.base.BaseActivity;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Tag;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IIconDetailsPresenter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsPresenter;
import com.edanichev.nounIcons.app.main.iconlist.view.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.EventBus.AuthEvent;
import com.edanichev.nounIcons.app.main.Utils.String.StringUtils;
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations;
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.BottomSheetView;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconShare;
import com.edanichev.nounIcons.app.main.Utils.UI.Toast.ToastShower;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipListener;

public class IconDetailsFragmentView extends BottomSheetDialogFragment implements IconDetailsFragmentViewInterface {
    private final static String ICON_KEY = "icon";

    private ProgressBar progress;
    private BottomSheetView bottomSheetImageView;
    private FlexboxLayout flexBox;
    private ChipCloud chipCloud;
    private TextView iconTermView;
    private ImageButton favoriteButton;
    private IconDetails iconData;
    private ImageButton shareButton;
    private Button iconButton;

    private BaseActivity activity;
    public IIconDetailsPresenter iconDetailsPresenter;

    public static IconDetailsFragmentView openIconDetails(IconDetails icon, FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ICON_KEY, icon);

        IconDetailsFragmentView bottomSheetFragment = new IconDetailsFragmentView();
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(fragmentManager, bottomSheetFragment.getTag());

        return bottomSheetFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        findView(myInflatedView);
        iconDetailsPresenter = new IconDetailsPresenter(this);
        receiveIconData();
        loadIconImage();
        loadIconTerm();
        showTags(iconData.getTags());
        loadFavoriteButton();
        loadShareButton();

        EventBus.getDefault().register(this);

        return myInflatedView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        iconDetailsPresenter.onDestroy();
        iconDetailsPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
        //bottomSheetImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessageOnAdd() {
        showMessage(getContext().getResources().getString(R.string.icon_added_to_favorites));
    }

    @Override
    public void showMessageOnRemove() {
        ToastShower.showDefaultToast(getContext().getResources().getString(R.string.icon_deleted_from_favorites), activity);
    }

    private void showMessage(String message) {
        ToastShower.showSuccessToast(message, activity);
    }

    @Subscribe
    @Override
    public void onAuthResult(AuthEvent event) {
        if (event.isSuccess()) {
            iconDetailsPresenter.onFavoriteButtonClick(iconData);
            showMessage("Hello " + NounFirebaseAuth.getCurrentUserName() + "!");
            animateButton(favoriteButton);
            activity.setMarkedBurger();
        }
        hideLoading();
    }

    @Override
    public void showAuthDialog() {
        iconDetailsPresenter.onAuthDialogShow();
        activity.showAuthDialog();
    }

    @Override
    public void setFavoriteButtonStatus(boolean isChecked) {
        if (isChecked) {
            favoriteButton.setImageDrawable(IconLoader.getCheckedFavoriteButton());
        } else {
            favoriteButton.setImageDrawable(IconLoader.getUncheckedFavoriteButton());
        }
        animateButton(favoriteButton);
    }

    public void shareIconImage() {
        IconShare.shareImage(bottomSheetImageView, activity);
    }

    @Override
    public void hideFavoriteButton() {
        favoriteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFavoriteButton() {
        favoriteButton.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        activity.hideLoadingDialog();
    }

    private void animateButton(final ImageButton button) {
        button.startAnimation(NounAnimations.getFavoriteButtonAnimation());
    }

    private void findView(View myInflatedView) {
        progress = myInflatedView.findViewById(R.id.progress_bar);
        bottomSheetImageView = myInflatedView.findViewById(R.id.bottom_sheet_view);
        iconButton = myInflatedView.findViewById(R.id.icon_button);
        flexBox = myInflatedView.findViewById(R.id.flexbox_drawable);
        iconTermView = myInflatedView.findViewById(R.id.icon_term);
        favoriteButton = myInflatedView.findViewById(R.id.add_to_favorite_button);
        shareButton = myInflatedView.findViewById(R.id.share_button);
        chipCloud = new ChipCloud(getContext(), flexBox, ChipConfig.getChipCloudConfig());
        chipCloud.setListener(onChipClickListener());
        favoriteButton.setOnClickListener(onFavoriteButtonClickListener());
        shareButton.setOnClickListener(onShareButtonClickListener());
        iconButton.setOnClickListener(onIconImageClickListener());
    }

    private void receiveIconData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            iconData = bundle.getParcelable(ICON_KEY);
        }
    }

    private void loadIconImage() {
        showProgress();
        String iconUrl;
        if (iconData.getAttribution_preview_url() == null)
            iconUrl = iconData.getPreview_url();
        else
            iconUrl = iconData.getAttribution_preview_url();

        Picasso.with(getContext())
                .load(iconUrl)
                .into(bottomSheetImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgress();
                        bottomSheetImageView.startAnimation(NounAnimations.getBecomeVisibleAnimation(NounAnimations.SHORT_FADE));
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    private void loadIconTerm() {
        iconTermView.setText(iconData.getTerm().toUpperCase());
    }

    private void showTags(List<Tag> tagList) {
        if (StringUtils.getTagsInString(tagList) != null)
            chipCloud.addChips(StringUtils.getTagsInString(tagList));
    }

    private void loadFavoriteButton() {
        hideFavoriteButton();
        iconDetailsPresenter.loadFavoriteStatus(iconData);
    }

    private void loadShareButton() {
        shareButton.setBackground(IconLoader.getShareButton());
    }

    private View.OnClickListener onFavoriteButtonClickListener() {
        return view -> iconDetailsPresenter.onFavoriteButtonClick(iconData);
    }

    private View.OnClickListener onIconImageClickListener() {
        return view -> dismiss();
    }

    private View.OnClickListener onShareButtonClickListener() {
        return view -> shareIconImage();
    }

    private ChipListener onChipClickListener() {
        return (i, b, b1) -> {
            if (b) {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).searchIconsList(chipCloud.getLabel(i));
                } else {
                    return;
                }
                dismiss();
            }
        };
    }
}