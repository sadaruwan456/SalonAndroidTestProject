package com.dilanka456.myprojectsalonapp10.Model;

public class Customer {
    String name;
    String mobile;
    String email;
    String password;
    String pro_pic_Url;
    String fcm_token;
    String status;


    public Customer() {
    }


    public Customer(String name, String mobile, String email, String password, String pro_pic_Url, String fcm_token, String status) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.pro_pic_Url = pro_pic_Url;
        this.fcm_token = fcm_token;
        this.status = status;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPro_pic_Url() {
        return pro_pic_Url;
    }

    public void setPro_pic_Url(String pro_pic_Url) {
        this.pro_pic_Url = pro_pic_Url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
