package com.cncoding.teazer.injection.module.mainmodule.remote;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.cncoding.teazer.utilities.common.Annotations.BASE_URL_STRING;
import static com.cncoding.teazer.utilities.common.Annotations.GIPHY;
import static com.cncoding.teazer.utilities.common.Annotations.WITHOUT_AUTH_TOKEN;
import static com.cncoding.teazer.utilities.common.Annotations.WITH_AUTH_TOKEN;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module(includes = NetworkModule.class)
public class RetrofitModule {

    @Provides @Singleton @Named(WITH_AUTH_TOKEN)
    public Retrofit getRetrofitWithAuthToken(@Named(BASE_URL_STRING) String baseUrl, @Named(WITH_AUTH_TOKEN) OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides @Singleton @Named(WITHOUT_AUTH_TOKEN)
    Retrofit getRetrofitWithoutAuthToken(@Named(BASE_URL_STRING) String baseUrl, @Named(WITHOUT_AUTH_TOKEN) OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides @Singleton @Named(GIPHY)
    Retrofit getGiphyRetrofit(@Named(WITHOUT_AUTH_TOKEN) OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://api.giphy.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}