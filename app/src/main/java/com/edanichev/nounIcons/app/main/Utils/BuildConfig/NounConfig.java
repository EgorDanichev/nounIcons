package com.edanichev.nounIcons.app.main.Utils.BuildConfig;


import com.edanichev.nounIcons.app.BuildConfig;

public class NounConfig implements INounConfig {

    private static final NounConfig OUR_INSTANCE = new NounConfig();

    public static NounConfig getInstance() {
        return OUR_INSTANCE;
    }

    private NounConfig() {
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
