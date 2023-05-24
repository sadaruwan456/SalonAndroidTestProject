package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chaos.view.PinView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class send_enter_view extends AppCompatActivity {

    private static final String TAG = "OTP_sent_tag";
    Button verify_btn;
    PinView pinView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_enter_view);

        Bundle bundle = getIntent().getExtras();
        String sentOTP = bundle.getString("opt");
        String email = bundle.getString("email");
        String docId = bundle.getString("docId");

        pinView = findViewById(R.id.otp_pin_view);
        verify_btn = findViewById(R.id.verify_otp_btn);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentOTP = pinView.getText().toString();

                if (sentOTP.equals(currentOTP)){

                    Intent intent = new Intent(send_enter_view.this,Change_password.class);
                    intent.putExtra("email",email);
                    intent.putExtra("docId",docId);
                    startActivity(intent);
                    finish();


                }else{

                    MysalonListPopup();

                }

            }
        });
    }


    public void MysalonListPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View popAlert = getLayoutInflater().inflate(R.layout.popup_otp_wrong,null);

        Button try_btn = popAlert.findViewById(R.id.otp_wrong_try_btn);
        Button re_btn = popAlert.findViewById(R.id.otp_wrong_re_send_btn);
        dialogBuilder.setView(popAlert);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        try_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinView.setText(null);
                alertDialog.cancel();
            }
        });

        re_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(send_enter_view.this,ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });


    }



}