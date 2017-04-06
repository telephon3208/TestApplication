package com.masha.testapplication;


import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.masha.testapplication.API.API;
import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.Salt;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ServerAPI {

    private String login, password;

    ServerAPI(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public boolean getAccess() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.smiber.com/v4.005/")
                //Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        //создаем класс, который будет преобразован в тело сообщения
        Login log = new Login();
        log.setLogin(login);

        try {
            //отправляю запрос и получаю ответ res
            Call<Salt> call = api.getSalt(log);

           // Response<Salt> res = call.execute();

            call.enqueue(new Callback<Salt>() {
                 @Override
                 public void onResponse(Call<Salt> call, Response<Salt> response) {
                     Salt salt = response.body();
                     Log.d("MyLogs", "salt " + salt.getData());
                 }

                 @Override
                 public void onFailure(Call<Salt> call, Throwable t) {
                     Log.d("MyLogs", "упс ошибка");
                 }
             });
           // Salt salt = res.body();
          //  Log.d("MyLogs", res.body().toString());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyLogs", "соляная ошибка");
        }

        return false;
    }











}

