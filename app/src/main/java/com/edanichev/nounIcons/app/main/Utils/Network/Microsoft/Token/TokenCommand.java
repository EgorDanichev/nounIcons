package com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Token;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TokenCommand {

    private Retrofit retrofit;
    private TokenService service;

    public TokenCommand() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cognitive.microsoft.com/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        service = retrofit.create(TokenService.class);

    }

    public String token() throws IOException {

        Call<String> call = service.token();
        String token = call.execute().body();

        return token;

    }
}