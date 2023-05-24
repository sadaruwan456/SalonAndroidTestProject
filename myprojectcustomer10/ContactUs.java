package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 220;
    TextView call_fld, msg_fld;
    ImageView facebook_fld, instagram_fld, skype_fld, twitter_fld, viber_fld, whatsapp_fld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        call_fld = findViewById(R.id.call_mobile_fld);
        msg_fld = findViewById(R.id.message_mobile_fld);
        facebook_fld = findViewById(R.id.facebook_img_fld);
        instagram_fld = findViewById(R.id.instatgram_img_fld);
        skype_fld = findViewById(R.id.skype_img_fld);
        twitter_fld =findViewById(R.id.twitter_img_fld);
        viber_fld = findViewById(R.id.viber_img_fld);
        whatsapp_fld = findViewById(R.id.whatsapp_img_fld);

//-----------------------------------social Media Action--------------------------------------
//        String url = "https://play.google.com/store/movies";
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        startActivity(intent);

        facebook_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });


        instagram_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });


        skype_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.skype.com/en/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        twitter_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/?lang=en";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        viber_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.viber.com/en/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        whatsapp_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+"+94712944829";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


//        -------------------------------Message action------------------------------------
        msg_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                intent1.setData(Uri.parse("smsto:" + msg_fld.getText().toString()));
                intent1.putExtra("sms_body", "Hi i am Dilanka");
                startActivity(intent1);
            }
        });


//        ----------------------call action------------------------------------------------
        call_fld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeCall();
            }
        });
    }

    public void MakeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + call_fld.getText().toString()));
        if (ContextCompat.checkSelfPermission(ContactUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactUs.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MakeCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}