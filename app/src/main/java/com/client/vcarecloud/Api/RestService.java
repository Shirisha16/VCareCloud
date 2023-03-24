package com.client.vcarecloud.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestService {
    private static final String URL = "https://testdaycareonlinewebapi.azurewebsites.net/";
    //testing Envirnoment
    //private static final String URL = "http://hrvmswebapi.azurewebsites.net/api/HRVMS/";
    // production Envirnoment
    private retrofit.RestAdapter restAdapter;
    private VcareApi apiService;

    public RestService()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        apiService=retrofit.create(VcareApi.class);
    }

    public VcareApi getService()
    {
        return apiService;
    }
}
