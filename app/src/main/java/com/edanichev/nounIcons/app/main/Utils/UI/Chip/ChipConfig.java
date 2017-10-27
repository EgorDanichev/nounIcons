package com.edanichev.nounIcons.app.main.Utils.UI.Chip;


import android.graphics.Color;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;

public class ChipConfig {

    public static ChipCloudConfig getChipCloudConfig(){
        return new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)
                .checkedChipColor(Color.parseColor("#FF9800"))
                .checkedTextColor(Color.parseColor("#000000"))
                .uncheckedChipColor(Color.parseColor("#000000"))
                .uncheckedTextColor(Color.parseColor("#FF9800"))
                .useInsetPadding(true);
    }
}
