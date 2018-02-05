package com.cncoding.teazer.data.repository.remote.application;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.model.application.DeactivateTypes;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.base.Category;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface ApplicationRepository {

    LiveData<List<ReportPostTitlesResponse>> getPostReportTypes();

    LiveData<List<ReportPostTitlesResponse>> getProfileReportTypes();

    LiveData<List<DeactivateTypes>> getDeactivationTypesList();

    LiveData<ArrayList<Category>> getCategories();
}
