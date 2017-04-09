package com.masha.testapplication.API;

import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.Salt;
import com.masha.testapplication.ModelClasses.MyToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface API {
    @Headers("Content-Type: application/json")
    @POST("salt")
    Call<Salt> getSalt(@Body Login login);

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("oauth/token")
    //Call<MyToken> getToken(@Body Credentials credentials);
    Call<MyToken> getToken(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password);



}
