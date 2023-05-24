package com.dilanka456.myprojectcustomer10.Model;

public class Salon {
    String name;
    String slogan;
    String contact;
    String email;
    String address;
    String logo_path;
    String owner_docId;
    double location_lat;
    double location_lon;
    String fcm_token;
    boolean status;

    public Salon() {
    }

    public Salon(String name, String slogan, String contact, String email, String address, String logo_path, String owner_docId, double location_lat, double location_lon, String fcm_token, boolean status) {
        this.name = name;
        this.slogan = slogan;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.logo_path = logo_path;
        this.owner_docId = owner_docId;
        this.location_lat = location_lat;
        this.location_lon = location_lon;
        this.fcm_token = fcm_token;
        this.status = status;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getOwner_docId() {
        return owner_docId;
    }

    public void setOwner_docId(String owner_docId) {
        this.owner_docId = owner_docId;
    }

    public double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    public double getLocation_lon() {
        return location_lon;
    }

    public void setLocation_lon(double location_lon) {
        this.location_lon = location_lon;
    }
}
