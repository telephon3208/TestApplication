package com.masha.testapplication;


import android.util.Log;

import com.masha.testapplication.API.API;
import com.masha.testapplication.ModelClasses.Login;
import com.masha.testapplication.ModelClasses.ResponseFromServer;
import com.masha.testapplication.ModelClasses.MyToken;
import com.masha.testapplication.ModelClasses.VideoLinkResponse;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerHelper {

    private String login, password;
    private API api;
    private String hashPassword;
    private String access_token = "";
    Retrofit retrofit;


    ServerHelper(String login, String password) {
        this.login = login;
        this.password = password;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.smiber.com/v4.005/")
                //Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }

    ServerHelper(final String access_token) {
        this.access_token = access_token;

        //кастимизируем okhttp-клиент
        /*OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String bearerToken = "Bearer {" + access_token + "}";

                // Customize the request
                Request request = original.newBuilder()
                        .header("Accept", "multipart/form-data")
                        .header("Authorization", bearerToken)
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        });

        OkHttpClient client = httpClient.build();*/

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.smiber.com/v4.005/")
                //Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(GsonConverterFactory.create())
               // .client(client)
                .build();

        api = retrofit.create(API.class);
    }

    public int getAccess() {

        int code = 0;

        Login log = new Login();
        log.setLogin(login);

        try {
            //отправляю запрос и получаю соль
            Call<ResponseFromServer> call = api.getSalt(log);
            retrofit2.Response<ResponseFromServer> res = call.execute();
            code = res.code();
            if (code == 200) {
                String salt = res.body().getData().getSalt();
                Log.d("MyLogs", "соль = " + salt);
                hashPassword = getHash(getHash(password) + salt);
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
            Call<MyToken> call = api.getToken("password", login, digest);
            retrofit2.Response<MyToken> res = call.execute();
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

    public int postFile(File file) {
        int code = 0;

   //     Map<String,File> fileMap = new HashMap<>();
   //     fileMap.put("file", file);

        //MediaType.parse() возвращает объект MediaType
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //MultipartBody.Part body =
        //        MultipartBody.Part.createFormData("VIDEO", file.getName(), requestFile);

        String bearerToken = "Bearer " + access_token; //Bearer is authentication scheme
        RequestBody typeFile = RequestBody.create(
                        MediaType.parse("text/plain"), "VIDEO");

        Call<VideoLinkResponse> call = api.postVideo(bearerToken, requestFile, typeFile);
        try {
            retrofit2.Response<VideoLinkResponse> res = call.execute();
            if (res != null) {
                code = res.code(); //
                if (res.isSuccessful()) {
                    Log.d("MyLogs", "link = ");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyLogs", "ошибка отправки видео");

        }
        return code;
    }




}

