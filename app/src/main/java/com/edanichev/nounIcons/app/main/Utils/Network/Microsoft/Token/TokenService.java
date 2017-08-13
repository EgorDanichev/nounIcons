package com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Token;


import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface TokenService {
    @Headers("Ocp-Apim-Subscription-Key:bb9da2b1a6934e7fba61caa322c2d665")

    @POST("sts/v1.0/issueToken")
    Call<String> token ();
}


