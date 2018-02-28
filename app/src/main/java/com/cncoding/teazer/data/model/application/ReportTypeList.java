package com.cncoding.teazer.data.model.application;


import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReportTypeList {
    private ArrayList<ReportType> reportTypeList;

    public ReportTypeList(ArrayList<ReportType> reportTypeList) {
        this.reportTypeList = reportTypeList;
    }

    public ArrayList<ReportType> getReportTypeList() {
        return reportTypeList;
    }

}