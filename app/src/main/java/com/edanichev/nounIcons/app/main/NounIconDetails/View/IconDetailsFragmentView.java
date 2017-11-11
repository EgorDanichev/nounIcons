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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Tag;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IIconDetailsPresenter;
import com.edanichev.nounIcons.app.main.NounIconDetails.Presenter.IconDetailsPresenter;
import com.edanichev.nounIcons.app.main.NounIconsList.View.MainActivity;
import com.edanichev.nounIcons.app.main.Utils.Auth.FireBaseAuth.NounFirebaseAuth;
import com.edanichev.nounIcons.app.main.Utils.EventBus.AuthEvent;
import com.edanichev.nounIcons.app.main.Utils.String.StringUtils;
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations;
import com.edanichev.nounIcons.app.main.Utils.UI.Chip.ChipConfig;
import com.edanichev.nounIcons.app.main.Utils.UI.Dialog.DialogShower;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.BottomSheetView;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconLoader;
import com.edanichev.nounIcons.app.main.Utils.UI.Pictures.IconShare;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipListener;

public class IconDetailsFragmentView extends BottomSheetDialogFragment implements IconDetailsFragmentViewInterface {
    private final static String ICON_KEY = "icon";
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

    private Activity activity;
    public IIconDetailsPresenter iconDetailsPresenter;

    public static void openIconDetails(IconDetails icon, FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ICON_KEY, icon);
        BottomSheetDialogFragment bottomSheetFragment = new IconDetailsFragmentView();
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(fragmentManager, bottomSheetFragment.getTag());
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
        showTags(iconData.getTags());
        loadFavoriteButton();
        loadShareButton();

        EventBus.getDefault().register(this);

        return myInflatedView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                    FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
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
        showMessage(getContext().getResources().getString(R.string.icon_deleted_from_favorites));
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    @Override
    public void onAuthResult(AuthEvent event) {
        if (event.isSucess()) {
            iconDetailsPresenter.onFavoriteButtonClick(iconData);
            showMessage("Hello " + NounFirebaseAuth.getCurrentUserName() + "!");
            animateButton(favoriteButton);
        }
        DialogShower.hideLoadingDialog();
    }

    @Override
    public void showAuthDialog() {
        DialogShower.showAuthDialog(getContext());
        iconDetailsPresenter.onAuthDialogShow();
        showLoaderDialog();
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

    @Override
    public void showLoaderDialog() {
        DialogShower.showLoadingDialog(getContext());
    }

    private void animateButton(final ImageButton button) {
        button.startAnimation(NounAnimations.getFavoriteButtonAnimation());
    }

    private void createPresenter() {
        iconDetailsPresenter = new IconDetailsPresenter(this);
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
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconDetailsPresenter.onFavoriteButtonClick(iconData);
            }
        };
    }

    private View.OnClickListener onIconImageClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    private View.OnClickListener onShareButtonClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                shareIconImage();
            }
        };
    }

    private ChipListener onChipClickListener() {
        return new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if (b) {
                    try {
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).searchIconsList(chipCloud.getLabel(i));
                        } else {
                            return;
                        }
                    } catch (IOException | ExecutionException | InterruptedException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                }
            }
        };
    }

}
