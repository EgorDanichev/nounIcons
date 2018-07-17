package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;


import io.reactivex.Single;

public interface IGetIconsCommand {

    Single getIconsList(final String term);
}
