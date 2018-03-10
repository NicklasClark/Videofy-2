package com.cncoding.teazer.injection.auth.module;

import android.content.Context;

import com.cncoding.teazer.R;
import com.cncoding.teazer.injection.scope.AuthScope;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.cncoding.teazer.utilities.common.Annotations.BASE_URL_STRING;
import static com.cncoding.teazer.utilities.common.Annotations.WITHOUT_AUTH_TOKEN;

/**
 *
 * Created by Prem$ on 3/9/2018.
 */

@Module
public class AuthNetworkModule {

//    @Provides @AuthScope
//    HttpLoggingInterceptor getLoggingInterceptor() {
//        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//    }

    @Provides @AuthScope @Named(BASE_URL_STRING)
    String getBaseUrl(Context context) {
        return context.getString(R.string.base_url);
    }

    @Provides @AuthScope @Named(WITHOUT_AUTH_TOKEN)
    OkHttpClient getOkHttpClientWithoutAuthToken() {
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    @Provides @AuthScope @Named(WITHOUT_AUTH_TOKEN)
    Retrofit getRetrofitWithoutAuthToken(@Named(BASE_URL_STRING) String baseUrl, @Named(WITHOUT_AUTH_TOKEN) OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}