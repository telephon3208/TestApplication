package com.masha.testapplication;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

        InterfaceAPI api = retrofit.create(InterfaceAPI.class);
        //создаем класс, который будет преобразован в тело сообщения
        SaltBody body = new SaltBody();
        body.login = login;

        Call<SaltResponse> callRes = api.getSalt(body);
        try {
            Response<SaltResponse> res = callRes.execute();
            Log.d("MyLogs", res.body().token);
            return true;
        } catch (Exception e) {
            Log.d("MyLogs", "соляная ошибка");
        }


        return false;
    }





    interface InterfaceAPI {
        @POST("salt")
        Call<SaltResponse> getSalt(@Body SaltBody saltBody);
        //Call<> выносит метод в отдельный поток

        @POST("oauth/token")
        Call<RegisterResponse> registerUser(@Body RegisterBody registerBody);
    }

    private class SaltBody {
        public String login;
    }

    private class SaltResponse {
        public String token;
        //TODO: здесь посложнее будет
    }

    private class RegisterBody {
        public String login;
        public String password;
    }

    private class RegisterResponse {
        public String login;
    }





}

