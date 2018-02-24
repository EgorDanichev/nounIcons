package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList;

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons;
import com.edanichev.nounIcons.app.main.Utils.DB.Realm.IconsRealmAdapter;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GetIconsCommand implements IGetIconsCommand {

    private Retrofit retrofit;
    private IconsRealmAdapter iconsRealmAdapter;

    private NounIconListService service;

    @Inject
    public GetIconsCommand(Retrofit retrofit, IconsRealmAdapter iconsRealmAdapter) {
        this.retrofit = retrofit;
        this.iconsRealmAdapter = iconsRealmAdapter;

        service = retrofit.create(NounIconListService.class);
    }

    @Override
    public Single getIconsList(final String term) {

        final String requestUrl = retrofit.baseUrl().toString() + "icons/" + term;

        return Single.create(
                (SingleOnSubscribe<Icons>) e -> {
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

        );
    }

}
