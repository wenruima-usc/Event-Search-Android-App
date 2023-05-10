package com.csci571.hw9.models;

import com.csci571.hw9.fragments.SearchFragment;

import java.io.Serializable;

public class VenueDetail implements Serializable {
    String name;
    String address;
    String phoneNum;
    String openHours;
    String generalRule;
    String childRule;
    String lng;
    String lat;

    public VenueDetail(String name, String address, String phoneNum, String openHours, String generalRule, String childRule, String lng, String lat) {
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.openHours = openHours;
        this.generalRule = generalRule;
        this.childRule = childRule;
        this.lng = lng;
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public void setGeneralRule(String generalRule) {
        this.generalRule = generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

    @Override
    public String toString() {
        return "VenueDetail{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", openHours='" + openHours + '\'' +
                ", generalRule='" + generalRule + '\'' +
                ", childRule='" + childRule + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
