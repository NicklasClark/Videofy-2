package com.cncoding.teazer.model.react;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReportReaction {
    private int react_id;
    private int report_type_id;

    public ReportReaction(int react_id, int report_type_id) {
        this.react_id = react_id;
        this.report_type_id = report_type_id;
    }
}