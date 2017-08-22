package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import android.support.annotation.NonNull;

import com.edanichev.nounIcons.app.main.NounIconsList.IconsCallback;
import com.edanichev.nounIcons.app.main.Utils.Auth.OAuthInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetIconsCommand {

    private Retrofit retrofit;
    private NounIconListService service;
    private IconsCallback iconsCallback;

    public GetIconsCommand(IconsCallback callback){
        iconsCallback = callback;

        OAuthInterceptor oauthInterceptor = new OAuthInterceptor.Builder()
                .consumerKey("8d6f079d73054acab464cee59652d02f")
                .consumerSecret("d2cbb07a4cc34333a2485ca423d9faa6")
                .build();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(oauthInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.thenounproject.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(NounIconListService.class);

    }


    public void getIcons(String term) {

        service.icons(term).enqueue(
                new Callback<Icons>() {

                    @Override
                    public void onResponse(@NonNull Call<Icons> call, @NonNull Response<Icons> response) {
                        iconsCallback.onIconsSearchResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<Icons> call, Throwable t) {
                    }
                }
        );
    }
}
