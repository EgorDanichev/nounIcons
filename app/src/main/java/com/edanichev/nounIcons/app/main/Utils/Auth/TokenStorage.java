package com.edanichev.nounIcons.app.main.Utils.Auth;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class TokenStorage
{
    private static final String TOKEN_STORAGE = "tokenStorage";
    private static final String TOKEN_FIELD_NAME = "token";

    private SharedPreferences tokenStorage;

    private static TokenStorage instance;

    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new TokenStorage(context);
        }
    }
    public static TokenStorage getInstance() {
        return instance;
    }

    private TokenStorage(Context context)
    {
        tokenStorage = context.getSharedPreferences(TOKEN_STORAGE, MODE_PRIVATE);
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = tokenStorage.edit();
        editor.putString(TOKEN_FIELD_NAME, token);
        editor.apply();
    }

    public String getToken(){
        return tokenStorage.getString(TOKEN_FIELD_NAME,"");

    }




}