package com.cncoding.teazer.injection.module.mainmodule.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cncoding.teazer.R;
import com.cncoding.teazer.injection.module.common.ContextModule;
import com.cncoding.teazer.utilities.common.SharedPrefs;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.cncoding.teazer.utilities.common.Annotations.AUTH_TOKEN_STRING;
import static com.cncoding.teazer.utilities.common.Annotations.BASE_URL_STRING;
import static com.cncoding.teazer.utilities.common.Annotations.WITHOUT_AUTH_TOKEN;
import static com.cncoding.teazer.utilities.common.Annotations.WITH_AUTH_TOKEN;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module(includes = ContextModule.class)
public class NetworkModule {

    @Provides @Singleton HttpLoggingInterceptor getLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides @Singleton @Named(AUTH_TOKEN_STRING)
    public String getAuthToken(Context context) {
        return SharedPrefs.getAuthToken(context);
    }

    @Provides @Singleton @Named(BASE_URL_STRING)
    String getBaseUrl(Context context) {
        return context.getString(R.string.base_url);
    }

    @Provides @Singleton File getCacheFile(Context context) {
        return new File(context.getCacheDir(), "Response");
    }

    /**
     * 50MB cache
     */
    @Provides @Singleton Cache getCache(File directory) {
        return new Cache(directory, 50 * 1024 * 1024);
    }

    @Provides @Singleton Interceptor getInterceptor(@Named(AUTH_TOKEN_STRING) final String authToken) {
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

    @Provides @Singleton @Named(WITH_AUTH_TOKEN)
    public OkHttpClient getOkHttpClientWithAuthToken(HttpLoggingInterceptor loggingInterceptor, Interceptor interceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides @Singleton @Named(WITHOUT_AUTH_TOKEN)
    OkHttpClient getOkHttpClientWithoutAuthToken(HttpLoggingInterceptor loggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }
}