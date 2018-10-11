package com.edanichev.nounIcons.app.main.Utils.SharedPreferences;


import android.content.Context;
import android.content.SharedPreferences;

import com.edanichev.nounIcons.app.main.Utils.EventBus.NounApiConfigEvent;

import static android.content.Context.MODE_PRIVATE;

public class NounSharedPreferences implements  INounSharedPreferences {
    private static final String TOKEN_STORAGE = "sharedPreferences";
    private static final String TOKEN_FIELD_NAME = "token";
    private static final String HINT_IS_SEEN_FIELD_NAME = "hintIsSeen";
    private static final String AUTH_DIALOG_SHOWN = "authDialogShown";
    private static final String NOUN_API_KEY = "noun_key";
    private static final String NOUN_API_SECRET = "noun_secret";

    private static SharedPreferences sharedPreferences;

    private static NounSharedPreferences instance;

    public static void initInstance(Context context) {
        sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, MODE_PRIVATE);
    }

    public static NounSharedPreferences getInstance() {
        if (instance == null) {
            instance = new NounSharedPreferences();
        }
        return instance;
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_FIELD_NAME, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_FIELD_NAME, "");
    }

    public void setHintSeen(boolean isSeen) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HINT_IS_SEEN_FIELD_NAME, isSeen);
        editor.apply();
    }

    public boolean isHintSeen() {
        return sharedPreferences.getBoolean(HINT_IS_SEEN_FIELD_NAME, false);
    }

    public void setNounApiConfig(String key, String secret) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NOUN_API_KEY, key);
        editor.putString(NOUN_API_SECRET, secret);
        editor.apply();
    }

    public NounApiConfigEvent getNounApiConfig() {
        String key = sharedPreferences.getString(NOUN_API_KEY, "8d6f079d73054acab464cee59652d02f");
        String secret = sharedPreferences.getString(NOUN_API_SECRET, "ede7fa4a5090413ba11d6ffe0eb96f36");
        return new NounApiConfigEvent(key, secret);
    }

}