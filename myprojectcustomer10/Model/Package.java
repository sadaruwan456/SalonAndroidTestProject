package com.dilanka456.myprojectcustomer10.Model;

public class Package {
    String name;
    String description;
    String category;
    String age;
    String duration;
    String price;
    String img_url;
    String salon_doc_id;

    public Package() {
    }


    public Package(String name, String description, String category, String age, String duration, String price, String img_url, String salon_doc_id) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.age = age;
        this.duration = duration;
        this.price = price;
        this.img_url = img_url;
        this.salon_doc_id = salon_doc_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSalon_doc_id() {
        return salon_doc_id;
    }

    public void setSalon_doc_id(String salon_doc_id) {
        this.salon_doc_id = salon_doc_id;
    }
}
