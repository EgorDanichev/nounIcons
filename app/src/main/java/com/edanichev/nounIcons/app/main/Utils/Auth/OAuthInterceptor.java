package com.edanichev.nounIcons.app.main.Utils.Auth;

import com.edanichev.nounIcons.app.main.Utils.Auth.OAuth1.OauthConstants.ParameterList;
import com.edanichev.nounIcons.app.main.Utils.Auth.OAuth1.services.HMACSha1SignatureService;
import com.edanichev.nounIcons.app.main.Utils.Auth.OAuth1.services.TimestampServiceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OAuthInterceptor implements Interceptor {

    private final String consumerKey;
    private final String consumerSecret;


    private OAuthInterceptor(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        final String nonce = new com.edanichev.nounIcons.app.main.Utils.Auth.OAuth1.services.TimestampServiceImpl().getNonce();
        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
//        Log.d("nonce", nonce);
//        Log.d("time", timestamp);

        String dynamicStructureUrl = original.url().scheme() + "://" + original.url().host() + original.url().encodedPath();

//        Log.d("ENCODED PATH", ""+dynamicStructureUrl);
        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
//        Log.d("firstBaseString", firstBaseString);
        String generatedBaseString;


        if (original.url().encodedQuery() != null) {
            generatedBaseString = original.url().encodedQuery() + "&oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
        } else {
            generatedBaseString = "oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";

        }

        ParameterList result = new ParameterList();
        result.addQuerystring(generatedBaseString);
        generatedBaseString = result.sort().asOauthBaseString();
//        Log.d("Sorted","00--"+result.sort().asOauthBaseString());

        String secoundBaseString = "&" + generatedBaseString;

        if (firstBaseString.contains("%3F")) {
//            Log.d("iff","yess iff");
            secoundBaseString = "%26" + urlEncoded(generatedBaseString);
        }

        String baseString = firstBaseString + secoundBaseString;

        String signature = new HMACSha1SignatureService().getSignature(baseString, consumerSecret, "");

//        Log.d("consumerSecret", consumerSecret);
//        Log.d("Signature", signature);

        String AuthHeader = "OAuth oauth_consumer_key=\"" + consumerKey + "\", oauth_nonce=\"" + nonce + "\", oauth_signature=\"" + signature + "\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"" + timestamp + "\", oauth_version=\"1.0\"";

        Request.Builder requestBuilder = original.newBuilder()
                .addHeader("Authorization", AuthHeader);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }


    public static final class Builder {
        private String consumerKey;
        private String consumerSecret;

        public Builder consumerKey(String consumerKey) {
            if (consumerKey == null) throw new NullPointerException("consumerKey = null");
            this.consumerKey = consumerKey;
            return this;
        }

        public Builder consumerSecret(String consumerSecret) {
            if (consumerSecret == null) throw new NullPointerException("consumerSecret = null");
            this.consumerSecret = consumerSecret;
            return this;
        }

        public OAuthInterceptor build() {

            if (consumerKey == null) throw new IllegalStateException("consumerKey not set");
            if (consumerSecret == null) throw new IllegalStateException("consumerSecret not set");

            return new OAuthInterceptor(consumerKey, consumerSecret);
        }
    }

    public String urlEncoded(String url) {
        String encodedurl = "";
        try {
            encodedurl = URLEncoder.encode(url, "UTF-8");
//            Log.d("TEST", encodedurl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedurl;
    }
}