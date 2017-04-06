package com.masha.testapplication.API;

import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.Salt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {
    @Headers("Content-Type: application/json")
    @POST("salt")
    Call<Salt> getSalt(@Body Login login);

   // Call<Salt> getSalt(@Field("login") String login);


}
