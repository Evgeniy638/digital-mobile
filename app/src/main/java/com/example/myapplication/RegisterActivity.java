package com.example.myapplication;

import android.annotation.SuppressLint;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Callback {
    EditText firstNameEditText;
    EditText secondNameEditText;
    EditText patronymicEditText;
    EditText loginEditText;
    EditText password1EditText;
    EditText password2EditText;

    private Context context;

    private String loginText;
    private String passwordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        context = this;

        //сохраняем поля editText
        firstNameEditText = findViewById(R.id.first_name);
        secondNameEditText = findViewById(R.id.second_name);
        patronymicEditText = findViewById(R.id.patronymic);
        loginEditText = findViewById(R.id.login_registration);
        password1EditText = findViewById(R.id.password1);
        password2EditText = findViewById(R.id.password2);

        //вешаем событие на кнопку зарегестрироваться
        findViewById(R.id.registration_to_register).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_to_register:
                register();
                break;
        }
    }

    @SuppressLint("ShowToast")
    void register() {
        String pass1 = password1EditText.getText().toString();
        String pass2 = password2EditText.getText().toString();

        if (!isCorrectPassword(pass1, pass2)) {
            Toast.makeText(this, "password1 и password2 должны быить равны и " +
                    "содержать минимум одну цифру," +
                    "одну маленькую букву и одну большую букву", Toast.LENGTH_SHORT);
            return;
        }

        loginText = loginEditText.getText().toString();
        passwordText = password1EditText.getText().toString();

        HTTTPHelper.register(this,
                loginText,
                passwordText,
                firstNameEditText.getText().toString(),
                secondNameEditText.getText().toString(),
                patronymicEditText.getText().toString());
    }

    //password1 и password2 равны и содержат минимум одну цифру,
    //одну маленькую букву и одну большую букву
    boolean isCorrectPassword(String password1, String password2) {
        return password1.matches(".*\\d.*") &&
                password1.equals(password2) &&
                !password1.toLowerCase().equals(password1) &&
                !password1.toUpperCase().equals(password1) &&
                password1.length() >= 6;
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

                    Toast.makeText(context, "Вы успешно зарегестрировались!\n" + result, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.KEY_LOGIN, loginText);
                    intent.putExtra(MainActivity.KEY_PASSWORD, passwordText);
                    setResult(LoginActivity.GO_TO_REGISTER, intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
