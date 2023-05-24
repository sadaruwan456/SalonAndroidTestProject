package com.dilanka456.myprojectsalonapp10.fcmHelper;


import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class FCMClient extends AsyncTask <String,String,String>{


    public void sendFCMMessage(String token,String Title,String Mes){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n   \"to\":\""+token+"\",\r\n   \"notification\":{\r\n      \"sound\":\"default\",\r\n      \"body\":\""+Mes+"\",\r\n      \"title\":\""+Title+"\",\r\n      \"content_available\":true,\r\n      \"priority\":\"high\"\r\n   }\r\n}");
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("authorization", "key=AAAAeejUkLI:APA91bEZlZZMsbkdQzj3zJpilBYFKswk32e33LBJxFvkojXOky8LW3oWAEkRN2-GzSdRUA9JAxRpmrlnZcqHdD-G5i_G6B3IdEmDo5x3agMWt5u96ojngJ3LQCnJbGInAc-Bik5HJ7YH")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected String doInBackground(String... strings) {
        sendFCMMessage(strings[0],strings[1],strings[2]);
        return null;
    }
}
