package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import com.edanichev.nounIcons.app.main.NounApp;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.Utils.DB.Realm.IconsRealmAdapter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
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

//    public void getIcons(String term) {
//        String requestUrl = retrofit.baseUrl().toString() + "icons/" + term;
//        if (iconsRealmAdapter.checkCacheExists(requestUrl)) {
//            EventBus.getDefault().post(new IconsListFromCommandEvent(iconsRealmAdapter.getIconsFromCache(requestUrl)));
//            return;
//        }
//
//        service.icons(term).enqueue(
//                new Callback<Icons>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Icons> call, @NonNull Response<Icons> response) {
//                        if (response.body() != null) {
//                            EventBus.getDefault().post(new IconsListFromCommandEvent(response.body().getIcons()));
//                            iconsRealmAdapter.addIconsToCache(call.request().url().toString(), response.body().getIcons());
//                        } else
//                            EventBus.getDefault().post(new EmptyIconsListFromCommandEvent(true));
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<Icons> call, Throwable t) {
//                    }
//                }
//        );
//
//    }

    public Single getIconsList(final String term) {

        final String requestUrl = retrofit.baseUrl().toString() + "icons/" + term;

        return Single.create(
                new SingleOnSubscribe<Icons>() {
                    @Override
                    public void subscribe(SingleEmitter<Icons> e) throws Exception {
                        if (iconsRealmAdapter.checkCacheExists(requestUrl)) {
                            e.onSuccess(iconsRealmAdapter.getIconsFromCache(requestUrl));
                            return;
                        }

                        Response response = service.icons(term).execute();
                        if (response.body() != null) {
                            e.onSuccess((Icons) response.body());
                            iconsRealmAdapter.addIconsToCache(requestUrl, ((Icons) response.body()).getIcons());
                        } else {
                            e.onError(new EmptyListException("Empty response"));
                        }

                    }
                }

        );
    }

}
