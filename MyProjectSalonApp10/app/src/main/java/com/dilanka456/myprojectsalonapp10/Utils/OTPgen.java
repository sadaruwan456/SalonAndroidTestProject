package com.dilanka456.myprojectsalonapp10.Utils;

import java.util.Random;

public class OTPgen {
    public static char[] generatorOTP(int length)
    {
//    System.out.print("Your OTP is : ");
        //Creating object of Random class
        Random obj = new Random();
        char[] otp = new char[length];
        for (int i=0; i<length; i++)
        {
            otp[i]= (char)(obj.nextInt(10)+48);
        }
        return otp;
    }
}
