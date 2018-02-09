package com.cncoding.teazer.data.api.calls.application;

import com.cncoding.teazer.model.application.DeactivateTypes;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.base.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface ApplicationService {

    /**
     * To get the post report types
     * */
    @GET("/api/v1/application/post/report/types")
    Call<List<ReportPostTitlesResponse>> getPostReportTypes();

    /**
     * To get the profile report types
     * */
    @GET("/api/v1/application/profile/report/types")
    Call<List<ReportPostTitlesResponse>> getProfileReportTypes();

    @GET("/api/v1/application/deactivate/types")
    Call<List<DeactivateTypes>> getDeactivationTypesList();

    /**
     * To get the categories list
     * */
    @GET("/api/v1/application/categories")
    Call<ArrayList<Category>> getCategories();
}