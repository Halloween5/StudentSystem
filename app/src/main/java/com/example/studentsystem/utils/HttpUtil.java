package com.example.studentsystem.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private static HttpUtil instance;
    private OkHttpClient client;

    private HttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookiesManager());
        client = builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            instance = new HttpUtil();
        }
        return instance;
    }

    public void loginPost(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        getOkHttpClient().newCall(request).enqueue(callback);
    }

    public void post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        getOkHttpClient().newCall(request).enqueue(callback);
    }

    public OkHttpClient getOkHttpClient() {
        return client;
    }

    private static class CookiesManager implements CookieJar {

        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<>();
        }
    }
}
