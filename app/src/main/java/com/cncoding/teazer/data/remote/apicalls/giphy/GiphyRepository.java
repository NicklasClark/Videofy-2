package com.cncoding.teazer.data.remote.apicalls.giphy;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.giphy.TrendingGiphy;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public interface GiphyRepository {

    LiveData<TrendingGiphy> getTrendingGiphys(String api_key, int limit, int offset, String rating);

    LiveData<TrendingGiphy> searchGiphy(String api_key, int limit, int offset, String rating, String lang, String query);

}