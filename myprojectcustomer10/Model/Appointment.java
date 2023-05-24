package com.dilanka456.myprojectcustomer10.Model;

public class Appointment {
    String CustomerDocId;
    String date;
    String time;
    String salonDocId;
    String packageDocId;
    String totalPrice;
    String status;


    public Appointment() {
    }

    public Appointment(String customerDocId, String date, String time, String salonDocId, String packageDocId, String totalPrice, String status) {
        CustomerDocId = customerDocId;
        this.date = date;
        this.time = time;
        this.salonDocId = salonDocId;
        this.packageDocId = packageDocId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerDocId() {
        return CustomerDocId;
    }

    public void setCustomerDocId(String customerDocId) {
        CustomerDocId = customerDocId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSalonDocId() {
        return salonDocId;
    }

    public void setSalonDocId(String salonDocId) {
        this.salonDocId = salonDocId;
    }

    public String getPackageDocId() {
        return packageDocId;
    }

    public void setPackageDocId(String packageDocId) {
        this.packageDocId = packageDocId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
