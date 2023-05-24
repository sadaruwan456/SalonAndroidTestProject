package com.dilanka456.myprojectcustomer10.Model;

public class Owner {
    String name;
    String mobile;
    String email;
    String password;
    String address;
    String pro_pic_url;
    String status;
    String auth_id;



    public Owner() {
    }

    public Owner(String name, String mobile, String email, String password, String address, String pro_pic_url, String status, String auth_id) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.address = address;
        this.pro_pic_url = pro_pic_url;
        this.status = status;
        this.auth_id = auth_id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPro_pic_url() {
        return pro_pic_url;
    }

    public void setPro_pic_url(String pro_pic_url) {
        this.pro_pic_url = pro_pic_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }
}
