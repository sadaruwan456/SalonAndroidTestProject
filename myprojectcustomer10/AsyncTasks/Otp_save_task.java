package com.dilanka456.myprojectcustomer10.AsyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.dilanka456.myprojectcustomer10.Model.CustomerOpt;
import com.dilanka456.myprojectcustomer10.OTP_enter;
import com.dilanka456.myprojectcustomer10.Utils.AppConfig;
import com.dilanka456.myprojectcustomer10.Utils.HttpPostClient;
import com.dilanka456.myprojectcustomer10.Utils.OTPgen;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Otp_save_task extends AsyncTask<String,String,String> {


    private static final String TAG = "OTP_save_task";
    private final View view ;
    private String otp;
    private String email;
    private String docID;

    public Otp_save_task(View v) {
        this.view =v;

    }


    @Override
    protected String doInBackground(String... strings) {
        email = strings[0];
        otp = strings[1];
        docID = strings[2];
        int otpnum = Integer.parseInt(otp);
        Log.d(TAG,"OTP: "+email+" "+otp);


        CustomerOpt customerOpt = new CustomerOpt();
        customerOpt.setEmail(email);
        customerOpt.setOtp(otpnum);

        Gson gson = new Gson();
        String toJson = gson.toJson(customerOpt);

        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("parameter", toJson));

        String response = new HttpPostClient().sendHttpPostRequest(AppConfig.customerOTP, list);

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Gson gson = new Gson();
        Boolean aBoolean = gson.fromJson(s, Boolean.TYPE);

        Log.d(TAG,"OTP_sts"+aBoolean);

        if (aBoolean){
            Intent intent = new Intent(view.getContext(), OTP_enter.class);
            intent.putExtra("opt",otp);
            intent.putExtra("email",email);
            intent.putExtra("docId", docID);
            view.getContext().startActivity(intent);
        }


    }
}
