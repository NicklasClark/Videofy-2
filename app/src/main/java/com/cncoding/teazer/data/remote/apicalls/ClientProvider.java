package com.cncoding.teazer.data.remote.apicalls;

import android.support.annotation.NonNull;

import com.cncoding.teazer.R;
import com.cncoding.teazer.base.TeazerApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class ClientProvider {

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static Retrofit retrofitWithAuthToken;
    private static Retrofit retrofitWithoutAuthToken;

    public static Retrofit getRetrofitWithAuthToken(@NonNull final String token) {
        if (retrofitWithAuthToken == null) {
//            retrofitWithoutAuthToken = null;
            retrofitWithAuthToken = new Retrofit.Builder()
                    .baseUrl(TeazerApplication.getContext().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientWithAuthToken(token))
                    .build();
        }
        return retrofitWithAuthToken;
    }

    public static Retrofit getRetrofitWithoutAuthToken() {
        if (retrofitWithoutAuthToken == null) {
//            retrofitWithAuthToken = null;
            retrofitWithoutAuthToken = new Retrofit.Builder()
                    .baseUrl(TeazerApplication.getContext().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofitWithoutAuthToken;
    }

    @NonNull private static OkHttpClient getOkHttpClientWithAuthToken(@NonNull final String authToken) {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
//                        try {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Authorization", "Bearer " + authToken)
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return null;
//                        }
                    }
                })
//                .addInterceptor(logging)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    @NonNull private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
//                .addInterceptor(logging)
                .build();
    }

    public static void clearRetrofitWithAuthToken() {
        ClientProvider.retrofitWithAuthToken = null;
    }
}
