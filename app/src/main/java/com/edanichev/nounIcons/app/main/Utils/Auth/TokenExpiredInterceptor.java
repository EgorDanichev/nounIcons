package com.edanichev.nounIcons.app.main.Utils.Auth;

import com.edanichev.nounIcons.app.main.NounIconsList.TokenStorage;
import com.edanichev.nounIcons.app.main.Utils.Network.Microsoft.Token.TokenCommand;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TokenExpiredInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();

        Request originalRequest = chain.request();

        if (response.code() == 400)
        {
            TokenCommand tokenCommand = new TokenCommand();
            String token = tokenCommand.token();
            TokenStorage.getInstance().setToken(token);

            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + TokenStorage.getInstance().getToken());
            Request newRequest = requestBuilder.build();

            return chain.proceed(newRequest);
        }
        else {
            return response;
        }

    }
}