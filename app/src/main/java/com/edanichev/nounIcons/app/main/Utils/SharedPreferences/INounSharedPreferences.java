package com.edanichev.nounIcons.app.main.Utils.SharedPreferences;

import com.edanichev.nounIcons.app.main.Utils.EventBus.NounApiConfigEvent;

public interface INounSharedPreferences {

    void setToken(String token);

    String getToken();

    void setHintSeen(boolean isSeen);

    boolean isHintSeen();

    void setNounApiConfig(String key, String secret);

    NounApiConfigEvent getNounApiConfig();
}