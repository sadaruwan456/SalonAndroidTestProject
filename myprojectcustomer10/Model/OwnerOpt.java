package com.dilanka456.myprojectcustomer10.Model;
// Generated Mar 12, 2021 9:19:33 AM by Hibernate Tools 4.3.1



/**
 * OwnerOpt generated by hbm2java
 */
public class OwnerOpt  implements java.io.Serializable {


    private int idownerOpt;
    private String email;
    private Integer otp;

    public OwnerOpt() {
    }


    public OwnerOpt(int idownerOpt) {
        this.idownerOpt = idownerOpt;
    }
    public OwnerOpt(int idownerOpt, String email, Integer otp) {
        this.idownerOpt = idownerOpt;
        this.email = email;
        this.otp = otp;
    }

    public int getIdownerOpt() {
        return this.idownerOpt;
    }

    public void setIdownerOpt(int idownerOpt) {
        this.idownerOpt = idownerOpt;
    }
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getOtp() {
        return this.otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }




}


