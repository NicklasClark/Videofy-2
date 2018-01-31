package com.cncoding.teazer.data.repository.remote.application;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.data.model.base.Category;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class ApplicationRepo implements ApplicationRepository {
    @Override
    public LiveData<List<ReportPostTitlesResponse>> getPostReportTypes() {
        return null;
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
