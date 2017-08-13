package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edanichev.nounIcons.app.R;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.squareup.picasso.Picasso;

public class MyFragment extends BottomSheetFragment {

    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.bottom_sheet, container, false);
        imageView = (ImageView) myInflatedView.findViewById(R.id.bottom_sheet_view);

        String str = getArguments().getString("text");

        Picasso.with(getActivity())
                .load(str)
                .into(imageView);

        return myInflatedView;
    }


}