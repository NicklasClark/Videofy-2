package com.cncoding.teazer.injection.home.base.module.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cncoding.teazer.R;
import com.cncoding.teazer.injection.scope.BaseScope;
import com.cncoding.teazer.utilities.common.SharedPrefs;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.cncoding.teazer.utilities.common.Annotations.AUTH_TOKEN_STRING;
import static com.cncoding.teazer.utilities.common.Annotations.BASE_URL_STRING;
import static com.cncoding.teazer.utilities.common.Annotations.WITHOUT_AUTH_TOKEN;
import static com.cncoding.teazer.utilities.common.Annotations.WITH_AUTH_TOKEN;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module
public class NetworkModule {

//    @Provides @BaseScope HttpLoggingInterceptor getLoggingInterceptor() {
//        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//    }

    @Provides @BaseScope
    @Named(AUTH_TOKEN_STRING)
    public String getAuthToken(Context context) {
        return SharedPrefs.getAuthToken(context);
    }

    @Provides @BaseScope
    @Named(BASE_URL_STRING)
    String getBaseUrl(Context context) {
        return context.getString(R.string.base_url);
    }

    @Provides @BaseScope
    File getCacheFile(Context context) {
        return new File(context.getCacheDir(), "Response");
    }

    /**
     * 50MB cache
     */
    @Provides @BaseScope
    Cache getCache(File directory) {
        return new Cache(directory, 50 * 1024 * 1024);
    }

    @Provides @BaseScope
    Interceptor getInterceptor(@Named(AUTH_TOKEN_STRING) final String authToken) {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                return chain.proceed(original.newBuilder()
                        .header("Authorization", "Bearer " + authToken)
                        .method(original.method(), original.body())
                        .build());
            }
        };
    }

    @Provides @BaseScope
    @Named(WITH_AUTH_TOKEN)
    OkHttpClient getOkHttpClientWithAuthToken(Interceptor authInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
//                .addInterceptor(loggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides @BaseScope
    @Named(WITHOUT_AUTH_TOKEN)
    OkHttpClient getOkHttpClientWithoutAuthToken(Cache cache) {
        return new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }
}