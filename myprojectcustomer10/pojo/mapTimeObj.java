package com.dilanka456.myprojectcustomer10.pojo;

public class mapTimeObj {
    public int getTimeInMins() {
        return timeInMins;
    }

    public void setTimeInMins(int timeInMins) {
        this.timeInMins = timeInMins;
    }

    public String getTimeInText() {
        return timeInText;
    }

    public void setTimeInText(String timeInText) {
        this.timeInText = timeInText;
    }

    public mapTimeObj(){}
    public mapTimeObj(int timeInMins, String timeInText) {
        this.timeInMins = timeInMins;
        this.timeInText = timeInText;
    }

    int timeInMins;
    String timeInText;
}
