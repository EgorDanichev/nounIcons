package com.edanichev.nounIcons.app.main.Utils.EventBus;

public class NounApiConfigEvent {

    private String noun_key;
    private String noun_secret;

    public NounApiConfigEvent(String noun_key, String noun_secret) {
        this.noun_key = noun_key;
        this.noun_secret = noun_secret;
    }

    public void setNoun_key(String noun_key) {

        this.noun_key = noun_key;
    }

    public void setNoun_secret(String noun_secret) {
        this.noun_secret = noun_secret;
    }

    public String getNoun_key() {

        return noun_key;
    }

    public String getNoun_secret() {
        return noun_secret;
    }
}
