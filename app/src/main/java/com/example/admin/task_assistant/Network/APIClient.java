package com.example.admin.task_assistant.Network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * APIClient handles all the Network APIRequests Requests using Retrofit Library
 **/

public class APIClient {


    private static final String BASE_URL ="https://orgone.solutions/task/";

    private static APIRequests apiRequests;


    // Singleton Instance of APIRequests
    public static APIRequests getInstance() {
        if (apiRequests == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();



            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            apiRequests = retrofit.create(APIRequests.class);

            return apiRequests;
        }
        else {
            return apiRequests;
        }
    }

}


