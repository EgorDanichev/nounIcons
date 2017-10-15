package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;


import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface NounIconListService {
    @GET("/icons/{term}")
    Call<Icons> icons (@Path("term") String term);
}