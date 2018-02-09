package com.cncoding.teazer.data.api.calls.application;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.model.application.DeactivateTypes;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.base.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.api.calls.ClientProvider.getRetrofitWithoutAuthToken;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class ApplicationRepositoryImpl implements ApplicationRepository {

    private ApplicationService applicationService;

    public ApplicationRepositoryImpl() {
        applicationService = getRetrofitWithoutAuthToken().create(ApplicationService.class);
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getPostReportTypes() {
        final MutableLiveData<List<ReportPostTitlesResponse>> liveData = new MutableLiveData<>();
        applicationService.getPostReportTypes().enqueue(new Callback<List<ReportPostTitlesResponse>>() {
            @Override
            public void onResponse(Call<List<ReportPostTitlesResponse>> call, Response<List<ReportPostTitlesResponse>> response) {
                if (response.code() == 300) {
                    liveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ReportPostTitlesResponse>> call, Throwable t) {

            }
        });
        return liveData;
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getProfileReportTypes() {
        return null;
    }

    @Override
    public LiveData<List<DeactivateTypes>> getDeactivationTypesList() {
        return null;
    }

    @Override
    public LiveData<ArrayList<Category>> getCategories() {
        return null;
    }
}
