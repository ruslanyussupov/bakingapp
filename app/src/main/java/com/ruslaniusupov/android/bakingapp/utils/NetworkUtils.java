package com.ruslaniusupov.android.bakingapp.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    public static final String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static String getResponseFromUrl(String stringUrl) {

        URL url = makeUrl(stringUrl);

        if (url == null) {
            return null;
        }

        String response = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                stringBuilder.append(line);

            }

            response = stringBuilder.toString();

        } catch (IOException e) {

            Log.e(LOG_TAG, "Can't get response from url: " + url, e);

        } finally {

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {

                try {
                    inputStream.close();
                } catch (IOException e) {

                    Log.e(LOG_TAG, "Can't close the Input Stream", e);

                }

            }

        }

        return response;

    }

    private static URL makeUrl(String stringUrl) {

        if (TextUtils.isEmpty(stringUrl)) {
            return null;
        }

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Can't create URL", e);
        }

        return url;

    }

    public static boolean hasNetworkConnection(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnectedOrConnecting();

        }

        return false;

    }

}
