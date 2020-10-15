package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.HTTTPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback {
    EditText loginEditText;
    EditText passwordEditText;

    Context context;

    private String loginText;
    private String passwordText;

    public static final int GO_TO_REGISTER = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        context = this;

        //находим поля с логином и паролем
        loginEditText = findViewById(R.id.login);
        passwordEditText = findViewById(R.id.password);

        //задаём событие на кнопки
        findViewById(R.id.enter).setOnClickListener(this);
        findViewById(R.id.no_account).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter:
                login();
                break;
            case R.id.no_account:
                goToRegister();
                break;
        }
    }

    //логинимся
    void login() {
        loginText = loginEditText.getText().toString();
        passwordText = passwordEditText.getText().toString();

        HTTTPHelper.login(this, loginText, passwordText);

    }

    //переходим на регистрацию
    void goToRegister() {
        startActivityForResult(
                new Intent(this, RegisterActivity.class),
                GO_TO_REGISTER
        );
    }

    //возвращаю новый логин и пароль для сохранения их
    void finishActivity(String login, String password) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_LOGIN, login);
        intent.putExtra(MainActivity.KEY_LOGIN, password);
        setResult(MainActivity.LOGIN_INTENT_CODE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GO_TO_REGISTER:
                assert data != null;
                finishActivity(data.getStringExtra(MainActivity.KEY_LOGIN),
                        data.getStringExtra(MainActivity.KEY_PASSWORD));
                break;
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //показывает сообщение об ошибке
                Toast.makeText(context, "Ошибка на сервере!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response.body() == null) {
                        throw new NullPointerException();
                    }

                    String result = response.body().string();

                    if (result.equals("Wrong login and/or password")) {
                        //показывает сообщение об ошибке
                        Toast.makeText(context, "Логин или пароль неверен!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(result);

                    Toast.makeText(context, "Вы успешно авторизовались!", Toast.LENGTH_SHORT).show();

                    finishActivity(loginText, passwordText);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
