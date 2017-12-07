package com.edanichev.nounIcons.app.main.Utils.UI.Chip;


import android.annotation.SuppressLint;
import android.graphics.Color;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounApp;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;

public class ChipConfig {

    @SuppressLint("ResourceType")
    public static ChipCloudConfig getChipCloudConfig() {
        return new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)
                .checkedChipColor(NounApp.app().getResources().getColor(R.color.main_color))
                .checkedTextColor(NounApp.app().getResources().getColor(R.color.black_color))
                .uncheckedChipColor(NounApp.app().getResources().getColor(R.color.black_color))
                .uncheckedTextColor(NounApp.app().getResources().getColor(R.color.main_color))
                .useInsetPadding(true);
    }
}
