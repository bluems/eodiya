package kr.ac.sunmoon.gijoe.camaps;

public class DetailFacilityData {
    public DetailFacilityData(String facility_name, String address_street, String weekday, String weekend, String closed, String paid, String apply, String tel, String url) {
        this.facility_name = facility_name;
        this.address_street = address_street;
        this.weekday = weekday;
        this.weekend = weekend;
        this.closed = closed;
        this.paid = paid;
        this.apply = apply;
        this.tel = tel;
        this.url = url;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public String getAddress_street() {
        return address_street;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getWeekend() {
        return weekend;
    }

    public String getClosed() {
        return closed;
    }

    public String getPaid() {
        return paid;
    }

    public String getApply() {
        return apply;
    }

    public String getTel() {
        return tel;
    }

    public String getUrl() {
        return url;
    }

    private String facility_name;
    private String address_street;
    private String weekday = "";
    private String weekend = "";
    private String closed;
    private String paid;        // 유료여부
    private String apply;       //신청방법
    private String tel;         //전화번호
    private String url;         //홈페이지 주소
}
