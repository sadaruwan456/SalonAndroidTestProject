package com.dilanka456.myprojectcustomer10.pojo;

public class mapDistanceObj {

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }


    public mapDistanceObj(String distanceText, int distanceValM) {
        this.distanceText = distanceText;
        this.distanceValM = distanceValM;
    }

    String distanceText;

    public int getDistanceValM() {
        return distanceValM;
    }

    public void setDistanceValM(int distanceValM) {
        this.distanceValM = distanceValM;
    }

    int distanceValM;
}
