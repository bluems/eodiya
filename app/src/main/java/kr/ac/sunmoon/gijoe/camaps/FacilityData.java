package kr.ac.sunmoon.gijoe.camaps;

import java.io.Serializable;

public class FacilityData implements Serializable {
    private int facility_id;
    private String facility_name;
    private String closed;
    private String weekday_start;
    private String weekday_end;
    private String weekend_start;
    private String weekend_end;
    private String paid;
    private int paid_std_time;
    private int usage_fee;
    private int overtime_std;
    private int overtime_usage_fee;
    private String application;
    private String tel;
    private String website;
    private int build_id;


    public FacilityData(int facility_id, String facility_name, String closed, String weekday_start, String weekday_end, String weekend_start, String weekend_end, String paid, int paid_std_time, int usage_fee, int overtime_std, int overtime_usage_fee, String application, String tel, String website, int build_id) {
        this.facility_id = facility_id;
        this.facility_name = facility_name;
        this.closed = closed;
        this.weekday_start = weekday_start;
        this.weekday_end = weekday_end;
        this.weekend_start = weekend_start;
        this.weekend_end = weekend_end;
        this.paid = paid;
        this.paid_std_time = paid_std_time;
        this.usage_fee = usage_fee;
        this.overtime_std = overtime_std;
        this.overtime_usage_fee = overtime_usage_fee;
        this.application = application;
        this.tel = tel;
        this.website = website;
        this.build_id = build_id;
    }

    public int getFacility_id() {
        return facility_id;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public String getClosed() {
        return closed;
    }

    public String getWeekday_start() {
        return weekday_start;
    }

    public String getWeekday_end() {
        return weekday_end;
    }

    public String getWeekend_start() {
        return weekend_start;
    }

    public String getWeekend_end() {
        return weekend_end;
    }

    public String getPaid() {
        return paid;
    }

    public int getPaid_std_time() {
        return paid_std_time;
    }

    public int getUsage_fee() {
        return usage_fee;
    }

    public int getOvertime_std() {
        return overtime_std;
    }

    public int getOvertime_usage_fee() {
        return overtime_usage_fee;
    }

    public String getApplication() {
        return application;
    }

    public String getTel() {
        return tel;
    }

    public String getWebsite() {
        return website;
    }

    public int getBuild_id() {
        return build_id;
    }


}
