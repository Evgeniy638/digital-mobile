package com.example.myapplication.utils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HTTTPHelper {
    private static final String baseUrl = "https://back228.herokuapp.com";

    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static void login(Callback callback, String login, String password) {
        RequestBody body = new FormBody.Builder()
                .add("login", login)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "/login")
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void register(Callback callback, String login, String password,
                                String firstName, String secondName, String patronymic) {
        RequestBody body = new FormBody.Builder()
                .add("login", login)
                .add("password", password)
                .add("name", firstName)
                .add("surname", secondName)
                .add("lastname", patronymic)
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "/register")
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }
}
