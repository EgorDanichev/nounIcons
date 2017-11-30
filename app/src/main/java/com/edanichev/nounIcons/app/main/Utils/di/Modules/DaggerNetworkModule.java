package com.edanichev.nounIcons.app.main.Utils.di.Modules;

import android.content.Context;

import com.edanichev.nounIcons.app.main.Utils.Auth.OAuthInterceptor;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.IInternetStatus;
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.InternetStatus;
import com.edanichev.nounIcons.app.main.Utils.SharedPreferences.NounSharedPreferences;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetworkModule {
    private String mBaseUrl;

    public DaggerNetworkModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    public OAuthInterceptor provideOAuthInterceptor() {
        return new OAuthInterceptor.Builder()
                .consumerKey(NounSharedPreferences.getInstance().getNounApiConfig().getNoun_key())
                .consumerSecret(NounSharedPreferences.getInstance().getNounApiConfig().getNoun_secret())
                .build();
    }

    @Provides
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public IInternetStatus provideIInternetStatus(Context context) {
        return new InternetStatus(context);
    }

}
