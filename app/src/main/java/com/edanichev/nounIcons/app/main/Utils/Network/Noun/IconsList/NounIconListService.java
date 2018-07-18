package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NounIconListService {
    @GET("/icons/{term}")
    Single<Icons> icons(@Path("term") String term);
}