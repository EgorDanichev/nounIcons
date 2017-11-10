package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import android.support.annotation.NonNull;

import com.edanichev.nounIcons.app.main.NounApp;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.Utils.DB.Realm.IconsRealmAdapter;
import com.edanichev.nounIcons.app.main.Utils.EventBus.EmptyIconsListEvent;
import com.edanichev.nounIcons.app.main.Utils.EventBus.IconsListEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetIconsCommand {
    @Inject
    Retrofit retrofit;
    @Inject
    IconsRealmAdapter iconsRealmAdapter;

    private NounIconListService service;
    private static GetIconsCommand instance;

    public static GetIconsCommand getInstance() {
        if (instance == null) {
            instance = new GetIconsCommand();
        }
        return instance;
    }

    public GetIconsCommand() {
        NounApp.app().getComponent().inject(this);
        service = retrofit.create(NounIconListService.class);
    }

    public void getIcons(String term) {
        String requestUrl = retrofit.baseUrl().toString() + "icons/" + term;
        if (iconsRealmAdapter.checkCacheExists(requestUrl)) {
            EventBus.getDefault().post(new IconsListEvent(iconsRealmAdapter.getIconsFromCache(requestUrl)));
            return;
        }

        service.icons(term).enqueue(
                new Callback<Icons>() {
                    @Override
                    public void onResponse(@NonNull Call<Icons> call, @NonNull Response<Icons> response) {
                        if (response.body() != null) {
                            EventBus.getDefault().post(new IconsListEvent(response.body().getIcons()));
                            iconsRealmAdapter.addIconsToCache(call.request().url().toString(), response.body().getIcons());
                        } else
                            EventBus.getDefault().post(new EmptyIconsListEvent(true));
                    }

                    @Override
                    public void onFailure(@NonNull Call<Icons> call, Throwable t) {
                    }
                }
        );
    }
}
