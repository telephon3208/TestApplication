package com.masha.testapplication;


import android.util.Log;

import com.masha.testapplication.API.API;
import com.masha.testapplication.ModelClasses.Credentials;
import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.Salt2;
import com.masha.testapplication.ModelClasses.MyToken;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerHelper {

    private String login, password;
    private API api;

    ServerHelper(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public boolean getAccess() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.smiber.com/v4.005/")
                //Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);

        Login log = new Login();
        log.setLogin("test");
        //log.setLogin(login);

        try {
            //отправляю запрос и получаю соль
            Call<Salt2> call = api.getSalt(log);
            Response<Salt2> res = call.execute();
            Log.d("MyLogs", "res = " + res.raw());
            Log.d("MyLogs", "res = " + res.raw().headers());
            if (res.code() == 200) {
                String salt = res.body().getData().getSalt();
                Log.d("MyLogs", "соль = " + salt);
                tokenCall(encrypt(salt));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyLogs", "соляная ошибка");
        }

        return false;
    }

    //запрашиваю токен
    private void tokenCall(String digest) {
        Credentials credentials = new Credentials();
        credentials.setGrantType("password");
        credentials.setUsername("test");
        credentials.setPassword(digest);

        Call<MyToken> call = api.getToken(credentials);
        Response<MyToken> res = call.execute();

    }

    //шифрую пароль
    private String encrypt(String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            StringBuilder text = new StringBuilder("sha-256(sha-256(123456)+");
            text.append(salt);
            text.append(")");
            md.update(text.toString().getBytes("UTF-8"));

            byte[] digest = md.digest();

            return digest.toString();
        } catch (Exception e) {
            Log.d("MyLogs", "ошибка шифрования пароля");
            e.printStackTrace();
            return null;
        }

    }









}

