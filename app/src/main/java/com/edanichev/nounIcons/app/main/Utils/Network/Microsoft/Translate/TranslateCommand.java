package com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Translate;

import com.edanichev.nounIcons.app.main.Utils.Auth.NounSharedPreferences;
import com.edanichev.nounIcons.app.main.NounIconsList.TranslateCallback;
import com.edanichev.nounIcons.app.main.Utils.Auth.TokenExpiredInterceptor;
import com.edanichev.nounIcons.app.main.Utils.XML.XmlInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class TranslateCommand {

    private Retrofit retrofit;
    private TranslateService service;
    TranslateCallback translateCallback;

    public TranslateCommand(TranslateCallback callback) {
        translateCallback = callback;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new XmlInterceptor())
                .addInterceptor(new TokenExpiredInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.microsofttranslator.com/")
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        service = retrofit.create(TranslateService.class);
    }

    public void translate(String text) {

        service.translate("en", text, "Bearer " + NounSharedPreferences.getInstance().getToken()).enqueue(
                new Callback<Translation>() {
                    @Override
                    public void onResponse(Call<Translation> call, Response<Translation> response) {

                        if (response.body() != null) {
                            translateCallback.onTranslateResponse(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Translation> call, Throwable t) {
                    }
                }
        );
    }
}
