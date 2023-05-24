package com.dilanka456.myprojectsalonapp10.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HttpPostClient {


    public String sendHttpPostRequest (String url, List data){


        String response = "";


        try {
            //create client first
            DefaultHttpClient httpClient = new DefaultHttpClient();

            //create request
            HttpPost post = new HttpPost(url);

            //set data list to request
            post.setEntity(new UrlEncodedFormEntity(data));

            //send request
            HttpResponse httpResponse =   httpClient.execute(post);

            HttpEntity entity = httpResponse.getEntity();

            if (entity!=null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));

                String line = "";

                while ((line = bufferedReader.readLine()) !=null){

                    response = response+line;
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }


}
