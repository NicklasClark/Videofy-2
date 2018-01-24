package com.cncoding.teazer.data.repository;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.data.model.base.Category;

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
