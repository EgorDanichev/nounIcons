package com.edanichev.nounIcons.app.main.Utils.XML;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class XmlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        String wrappedBody = "<root>" + body.string() + "</root>";
        return response.newBuilder()
                .body(ResponseBody.create(body.contentType(), wrappedBody))
                .build();
    }
}