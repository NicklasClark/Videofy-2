package com.cncoding.teazer.model.application;


import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReportType {
    private int report_type_id;
    private String title;
    private ArrayList<ReportType> sub_reports;

    public ReportType(int report_type_id, String title, ArrayList<ReportType> sub_reports) {
        this.report_type_id = report_type_id;
        this.title = title;
        this.sub_reports = sub_reports;
    }

    public int getReport_type_id() {
        return report_type_id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<ReportType> getSub_reports() {
        return sub_reports;
    }
}