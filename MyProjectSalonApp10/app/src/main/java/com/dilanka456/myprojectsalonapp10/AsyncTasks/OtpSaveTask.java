package com.dilanka456.myprojectsalonapp10.AsyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import com.dilanka456.myprojectsalonapp10.Model.OwnerOpt;
import com.dilanka456.myprojectsalonapp10.Utils.AppConfig;
import com.dilanka456.myprojectsalonapp10.Utils.HttpPostClient;
import com.dilanka456.myprojectsalonapp10.send_enter_view;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class OtpSaveTask extends AsyncTask<String,String,String> {

    private final View view;
    private String otp;
    private String email;
    private String docID;

    public OtpSaveTask(View v) {
        this.view = v;
    }

    @Override
    protected String doInBackground(String... strings) {

        email = strings[0];
        otp = strings[1];
       docID = strings[2];

        int otpnum = Integer.parseInt(otp);

        OwnerOpt ownerOpt = new OwnerOpt();
        ownerOpt.setEmail(email);
        ownerOpt.setOtp(otpnum);


        Gson gson = new Gson();
        String toJson = gson.toJson(ownerOpt);

        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("parameter", toJson));

        String response = new HttpPostClient().sendHttpPostRequest(AppConfig.Ownerotp, list);

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Gson gson = new Gson();
        Boolean aBoolean = gson.fromJson(s, Boolean.TYPE);
        Log.d("OtpOk",aBoolean+"");

        if (aBoolean){
            Intent intent = new Intent(view.getContext(), send_enter_view.class);
            intent.putExtra("opt",otp);
            intent.putExtra("email",email);
            intent.putExtra("docId", docID);
            view.getContext().startActivity(intent);
        }
    }
}
