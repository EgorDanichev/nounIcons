package com.edanichev.nounIcons.app.main.Utils.Auth;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class NounSharedPreferences

{
    private static final String TOKEN_STORAGE = "sharedPreferences";
    private static final String TOKEN_FIELD_NAME = "token";
    private static final String HINT_IS_SEEN_FIELD_NAME = "hintIsSeen";

    private SharedPreferences sharedPreferences;

    private static NounSharedPreferences instance;

    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new NounSharedPreferences(context);
        }
    }

    public static NounSharedPreferences getInstance() {
        return instance;
    }

    private NounSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, MODE_PRIVATE);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_FIELD_NAME, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_FIELD_NAME,"");

    }

    public void setHintSeen(boolean isSeen) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HINT_IS_SEEN_FIELD_NAME, isSeen);
        editor.apply();
    }

    public boolean isHintSeen() {
        return sharedPreferences.getBoolean(HINT_IS_SEEN_FIELD_NAME,false);

    }

}