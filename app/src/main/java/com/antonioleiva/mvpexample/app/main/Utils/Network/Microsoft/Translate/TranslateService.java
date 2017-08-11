package com.antonioleiva.mvpexample.app.main.Utils.Network.Microsoft.Translate;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface TranslateService {


    @GET("/V2/Http.svc/Translate")
    Call<Translation> translate (@Query("to") String to, @Query("text") String text, @Header("Authorization") String auth);
}