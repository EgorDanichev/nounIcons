package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconDetail;

import com.edanichev.nounIcons.app.main.NounIconDetails.IconDetailsCallback;
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

public class GetIconDetailsCommand {

    private Retrofit retrofit;
    private NounIconService service;
    private IconDetailsCallback iconDetailsCallback;

    public GetIconDetailsCommand(IconDetailsCallback callback){
        iconDetailsCallback = callback;

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
        service = retrofit.create(NounIconService.class);

    }


    public void getIcon(String id) {

    service.getIcon(id).enqueue(
            new Callback<IconDetails>() {
                @Override
                public void onResponse(Call<IconDetails> call, Response<IconDetails> response) {
                    iconDetailsCallback.onIconsSearchResponse(response.body());
                }

                @Override
                public void onFailure(Call<IconDetails> call, Throwable t) {

                }
            }
    );
    }
}
