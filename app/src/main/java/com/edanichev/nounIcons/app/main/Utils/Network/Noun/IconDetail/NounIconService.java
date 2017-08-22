package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface NounIconService {
    @GET("/icon/{id}")
    Call<IconDetails> getIcon (@Path("id") String id);
}