package kr.ac.sunmoon.gijoe.camaps;

import java.io.Serializable;

public class BuildingData implements Serializable {
    private int build_id;
    private String build_name;
    private double latitude;
    private double longitude;
    private String address_street;

    public BuildingData(int build_id, String build_name, double latitude, double longitude, String address_street) {
        this.build_id = build_id;
        this.build_name = build_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address_street = address_street;
    }

    public int getBuild_id() {
        return build_id;
    }

    public String getBuild_name() {
        return build_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress_street() {
        return address_street;
    }
}
