package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import com.edanichev.nounIcons.app.main.Utils.Auth.OAuthInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetworkModule {
    private String mBaseUrl;

    public DaggerNetworkModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public OAuthInterceptor provideOAuthInterceptor() {
        return new OAuthInterceptor.Builder()
                .consumerKey("8d6f079d73054acab464cee59652d02f")
                .consumerSecret("ede7fa4a5090413ba11d6ffe0eb96f36")
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(OAuthInterceptor oauthInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(oauthInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
