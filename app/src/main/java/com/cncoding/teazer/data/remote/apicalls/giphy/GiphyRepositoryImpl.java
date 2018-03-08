package com.cncoding.teazer.data.remote.apicalls.giphy;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.model.giphy.TrendingGiphy;

import javax.inject.Inject;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.trendingGiphyCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getGiphyRetrofit;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_TRENDING_GIPHYS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SEARCH_GIPHYS;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class GiphyRepositoryImpl implements GiphyRepository {

    private GiphyService giphyService;

    @Inject public GiphyRepositoryImpl(GiphyService giphyService) {
        this.giphyService = giphyService;
    }

    public GiphyRepositoryImpl() {
        giphyService = getGiphyRetrofit().create(GiphyService.class);
    }

    @Override
    public LiveData<TrendingGiphy> getTrendingGiphys(String api_key, int limit, int offset, String rating) {
        final MutableLiveData<TrendingGiphy> liveData = new MutableLiveData<>();
        giphyService.getTrendingGiphys(api_key, limit, offset, rating).enqueue(trendingGiphyCallback(liveData, CALL_GET_TRENDING_GIPHYS));
        return liveData;
    }

    @Override
    public LiveData<TrendingGiphy> searchGiphy(String api_key, int limit, int offset, String rating, String lang, String query) {
        final MutableLiveData<TrendingGiphy> liveData = new MutableLiveData<>();
        giphyService.searchGiphy(api_key, limit, offset, rating, lang, query).enqueue(trendingGiphyCallback(liveData, CALL_SEARCH_GIPHYS));
        return liveData;
    }
}