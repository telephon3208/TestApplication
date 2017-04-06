package com.masha.testapplication.API;

import com.masha.testapplication.ModelClasses.Credentials;
import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.Salt2;
import com.masha.testapplication.ModelClasses.MyToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface API {
    @Headers("Content-Type: application/json")
    @POST("salt")
    Call<Salt2> getSalt(@Body Login login);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("oauth/token")
    Call<MyToken> getToken(@Body Credentials credentials);



}
