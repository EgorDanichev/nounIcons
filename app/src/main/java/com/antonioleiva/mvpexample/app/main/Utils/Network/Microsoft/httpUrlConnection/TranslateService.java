package com.antonioleiva.mvpexample.app.main.Utils.Network.Microsoft.httpUrlConnection;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TranslateService extends AsyncTask<String, Void, String> {

    TranslationResponseCallback callback;

    static String TOKEN ="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6Imh0dHBzOi8vYXBpLm1pY3Jvc29mdHRyYW5zbGF0b3IuY29tLyIsInN1YnNjcmlwdGlvbi1pZCI6IjU1NTQ1MjhlOGExNjQxM2RhNDAzYjI0ZjkxZDI4NDVmIiwicHJvZHVjdC1pZCI6IlRleHRUcmFuc2xhdG9yLkYwIiwiY29nbml0aXZlLXNlcnZpY2VzLWVuZHBvaW50IjoiaHR0cHM6Ly9hcGkuY29nbml0aXZlLm1pY3Jvc29mdC5jb20vaW50ZXJuYWwvdjEuMC8iLCJhenVyZS1yZXNvdXJjZS1pZCI6Ii9zdWJzY3JpcHRpb25zL2QyNmRlZDI4LTg3YTYtNGU1MC1hZjEyLWUyOTc1MjBmYTU2Mi9yZXNvdXJjZUdyb3Vwcy9Ob3J0aEV1cm9wZS9wcm92aWRlcnMvTWljcm9zb2Z0LkNvZ25pdGl2ZVNlcnZpY2VzL2FjY291bnRzL05vdW5UcmFuc2xhdG9yIiwiaXNzIjoidXJuOm1zLmNvZ25pdGl2ZXNlcnZpY2VzIiwiYXVkIjoidXJuOm1zLm1pY3Jvc29mdHRyYW5zbGF0b3IiLCJleHAiOjE1MDIxOTU3NDN9.SVap9B4rXXs1WRgYkpFLOmldBBkdZKf9acUVgQY0kPA";

    static String SECRET_KEY_VALUE ="bb9da2b1a6934e7fba61caa322c2d665";
    static String SECRET_KEY_HEADER ="Ocp-Apim-Subscription-Key";
    static final String TOKEN_EXPIRED_STRING = "The incoming token has expired";

    private final static String MICROSOFT_BASE_URL = "https://api.microsofttranslator.com/V2/Http.svc/";
    private final static String TRANSLATE_PATH = "Translate";
    private final static String QUERY_PARAM_TO = "en";


    public TranslateService(TranslationResponseCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        URL searchUrl = buildUrlForTranslate(params[0]);
        String translateResult = null;
        try {

            translateResult = doHttpUrlConnectionAction(searchUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return translateResult;
    }

    @Override
    protected void onPostExecute(String translateResults) {

        if (translateResults != null && !translateResults.equals("")) {
            callback.onResponseRecieve(translateResults);
        }
    }


    private String doHttpUrlConnectionAction(URL desiredUrl)
            throws Exception
    {
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try
        {
            HttpURLConnection connection = (HttpURLConnection) desiredUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", TOKEN);

            connection.setReadTimeout(15*1000);
            connection.connect();
            int responseCode = connection.getResponseCode();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private static URL buildUrlForTranslate (String text) {
        Uri builtUri = Uri.parse(MICROSOFT_BASE_URL).buildUpon()
                .appendPath(TRANSLATE_PATH)
                .appendQueryParameter("to",QUERY_PARAM_TO)
                .appendQueryParameter("text",text)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

}

