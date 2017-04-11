package com.masha.testapplication.API;

import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.ResponseFromServer;
import com.masha.testapplication.ModelClasses.MyToken;
import com.masha.testapplication.ModelClasses.VideoLinkResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface API {
    @Headers("Content-Type: application/json")
    @POST("salt")
    Call<ResponseFromServer> getSalt(@Body Login login);

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("oauth/token")
    Call<MyToken> getToken(@Field("grant_type") String grantType,
                           @Field("username") String username,
                           @Field("password") String password);

    @Multipart
    //@Headers("Content-Type: multipart/form-data; charset=utf-8; boundary=\"---BOUNDARY---\"") //без этого заголовка вылетает с ошибкой
    @Headers("Content-Type: multipart/form-data; boundary=\"---BOUNDARY---\"") //без этого заголовка вылетает с ошибкой
    @POST("file/upload")
    Call<VideoLinkResponse> postVideo(@Header("Authorization") String authorization,
                                      @Part ("file") RequestBody file,
                                    //  @PartMap Map<String,File> filesMap,
                                       @Part("type_file") RequestBody typeFile);
                                      //@Part("type_file") String typeFile);

    @Multipart
    @POST("file/upload")
    Call<ResponseBody> upload(@Header("Authorization") String authorization,
                              @Part MultipartBody.Part file,
                              @Part("type_file") RequestBody typeFile);

}
