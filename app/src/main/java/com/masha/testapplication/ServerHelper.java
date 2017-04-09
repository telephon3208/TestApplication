package com.masha.testapplication;


import android.util.Log;

import com.masha.testapplication.API.API;
import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.ResponseFromServer;
import com.masha.testapplication.ModelClasses.MyToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerHelper {

    private String login, password;
    private API api;
    private String hashPassword;
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
            Call<ResponseFromServer> call = api.getSalt(log);
            Response<ResponseFromServer> res = call.execute();
            code = res.code();
            if (code == 200) {
                String salt = res.body().getData().getSalt();
                Log.d("MyLogs", "соль = " + salt);
                hashPassword = getHash(getHash("123456") + salt);
                code = tokenCall(hashPassword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyLogs", "ошибка получения соли");

        }
        return code;
    }

    //запрашиваю токен
    private int tokenCall(String digest) {

        int code = 0;

        try {
            Call<MyToken> call = api.getToken("password", "test", digest);
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


    public String getAccessToken() {
        return access_token;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private String getHash(String password) {
        String hash = null;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            hash = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }





}

