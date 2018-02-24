package com.edanichev.nounIcons.app.main.NounIconDetails.Model;


import java.util.List;

public class Icons {

    private List<IconDetails> icons;

    public List<IconDetails> getIcons() {
        return icons;
    }

    public void setIcons(List<IconDetails> icons) {
        this.icons = icons;
    }

    public Icons(List<IconDetails> icons) {
        this.icons = icons;
    }

    public Icons() {
    }

}
