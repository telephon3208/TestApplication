package com.masha.testapplication;


import android.app.AlertDialog;
import android.util.Log;

import com.masha.testapplication.API.API;
import com.masha.testapplication.ModelClasses.Credentials;
import com.masha.testapplication.ModelClasses.Credentials2;
import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.Salt;
import com.masha.testapplication.ModelClasses.MyToken;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerHelper {

    private String login, password;
    private API api;

    private String access_token = "";

    ServerHelper(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public int getAccess() {

        int code = 0;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.smiber.com/v4.005/")
                //Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);

        Login log = new Login();
        log.setLogin("test");

        try {
            //отправляю запрос и получаю соль
            Call<Salt> call = api.getSalt(log);
            Response<Salt> res = call.execute();
            code = res.code();
            if (code == 200) {
                String salt = res.body().getData().getSalt();
                Log.d("MyLogs", "соль = " + salt);
                code = tokenCall(encrypt(salt));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyLogs", "ошибка получения соли");

        }
        return code;
    }

    //шифрую пароль
    private String encrypt(String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sha-256(sha-256(");
            stringBuilder.append("123456");
            stringBuilder.append(")+");
            stringBuilder.append(salt);
            stringBuilder.append(")");

            md.reset();
            md.update(stringBuilder.toString().getBytes("UTF-8"));
            byte[] digest = md.digest();
            String str = String.format("%0" + (digest.length*2) +
                    "X", new BigInteger(1, digest));
            Log.d("MyLogs", "digest = " + str);
            return str;

        } catch (Exception e) {
            Log.d("MyLogs", "ошибка шифрования пароля");
            e.printStackTrace();
            return null;
        }

    }

    //запрашиваю токен
    private int tokenCall(String digest) {

        if (digest.isEmpty()) {
            return 0;
        }

        int code = 0;

        Credentials credentials = new Credentials();
        credentials.setGrantType("password");
        credentials.setUsername("test");
        credentials.setPassword(digest);



        try {
            Call<MyToken> call = api.getToken(credentials);
            Response<MyToken> res = call.execute();
            code = res.code();
            if (code == 200) {
                access_token = res.body().getAccessToken();
                Log.d("MyLogs", "токен = " + access_token);
            }
        } catch (Exception e) {
            Log.d("MyLogs", "ошибка получения токена");
        }
        return code;

    }



    public String getAccess_token() {
        return access_token;
    }







}

