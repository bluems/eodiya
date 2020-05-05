package kr.ac.sunmoon.gijoe.camaps;

import java.io.Serializable;

// XML 데이터를 담기 위한 클래스
// C, Python Delphi 등의 Structure와 같은 기능을 위해 만들어졌다.
// Java에서 효율적인 데이터 관리를 위한 구현임.
class PublicData implements Serializable {
    private String place;       //개방장소명
    private String facility;    //개방시설명
    private String closed;      //휴관일
    private String weekday = "";     // 평일개방시간
    private String weekend = "";     // 주말개방시간
    private String paid;        // 유료여부
    private String desc;        //부대설명
    private String apply;       //신청방법
    private String address;     //주소
    private String tel;         //전화번호
    private String url;         //홈페이지 주소
    private double latitude;    //위도
    private double longitude;   //경도

    // 이하 위 변수의 Getter, Setter
    String getPlace() {
        return place;
    }

    void setPlace(String place) {
        this.place = place;
    }

    String getFacility() {
        return facility;
    }

    void setFacility(String facility) {
        this.facility = facility;
    }

    String getClosed() {
        return closed;
    }

    void setClosed(String closed) {
        this.closed = closed;
    }

    String getWeekday() {
        return weekday;
    }

    void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    String getWeekend() {
        return weekend;
    }

    void setWeekend(String weekend) {
        this.weekend = weekend;
    }

    String getPaid() {
        return paid;
    }

    void setPaid(String paid) {
        this.paid = paid;
    }

    String getDesc() {
        return desc;
    }

    void setDesc(String desc) {
        this.desc = desc;
    }

    String getApply() {
        return apply;
    }

    void setApply(String apply) {
        this.apply = apply;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    String getTel() {
        return tel;
    }

    void setTel(String tel) {
        this.tel = tel;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    double getLatitude() {
        return latitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    double getLongitude() {
        return longitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
