package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sPref;

    static final String KEY_LOGIN = "LOGIN";
    static final String KEY_PASSWORD = "PASSWORD";

    static final int LOGIN_INTENT_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPref = getPreferences(MODE_PRIVATE);

        //авторизация
        login();
    }


    void login() {
        //достаём кешированный логин и пароль
        final String DEFAULT_VALUE = "\"DEFAULT_VALUE\"";
        String login = sPref.getString(KEY_LOGIN, DEFAULT_VALUE);
        String password = sPref.getString(KEY_PASSWORD, DEFAULT_VALUE);

        //переход на LoginActivity если нет кешированных данных
        assert login != null;
        assert password != null;
        if (login.equals(DEFAULT_VALUE) || password.equals(DEFAULT_VALUE)) {
            startActivityForResult(
                    new Intent(this, LoginActivity.class),
                    LOGIN_INTENT_CODE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_INTENT_CODE:
                if (data != null) {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(KEY_LOGIN, data.getStringExtra(KEY_LOGIN));
                    ed.putString(KEY_PASSWORD, data.getStringExtra(KEY_PASSWORD));
                    ed.apply();
                }

                break;
        }
    }
}