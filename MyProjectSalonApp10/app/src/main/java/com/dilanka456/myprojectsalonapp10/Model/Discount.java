package com.dilanka456.myprojectsalonapp10.Model;

public class Discount {
    String salon_doc_id;
    String package_doc_id;
    String discount;

    public Discount() {
    }

    public Discount(String salon_doc_id, String package_doc_id, String discount) {
        this.salon_doc_id = salon_doc_id;
        this.package_doc_id = package_doc_id;
        this.discount = discount;
    }

    public String getSalon_doc_id() {
        return salon_doc_id;
    }

    public void setSalon_doc_id(String salon_doc_id) {
        this.salon_doc_id = salon_doc_id;
    }

    public String getPackage_doc_id() {
        return package_doc_id;
    }

    public void setPackage_doc_id(String package_doc_id) {
        this.package_doc_id = package_doc_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
