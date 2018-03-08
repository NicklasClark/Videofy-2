package com.cncoding.teazer.data.remote.apicalls.giphy;

import com.cncoding.teazer.data.model.giphy.TrendingGiphy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public interface GiphyService {

    @GET("/v1/gifs/trending")
    Call<TrendingGiphy> getTrendingGiphys(@Query("api_key") String api_key, @Query("limit") int limit,
                                          @Query("offset") int offset, @Query("rating") String rating);

    @GET("/v1/gifs/search")
    Call<TrendingGiphy> searchGiphy(@Query("api_key") String api_key, @Query("limit") int limit,
                                    @Query("offset") int offset, @Query("rating") String rating,
                                    @Query("lang") String lang, @Query("q") String query);

}