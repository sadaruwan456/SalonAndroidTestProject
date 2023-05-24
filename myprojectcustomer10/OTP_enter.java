package com.dilanka456.myprojectcustomer10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;

public class OTP_enter extends AppCompatActivity {

    TextView email_fld;
    PinView pinView;
    Button verify_btn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_enter);

        email_fld = findViewById(R.id.otp_email_fld);
        pinView = findViewById(R.id.otp_pin_view);
        verify_btn = findViewById(R.id.verify_otp_btn);

        Bundle bundle = getIntent().getExtras();
        String sendopt = bundle.getString("opt");
        String email = bundle.getString("email");
        String docId = bundle.getString("docId");

        email_fld.setText(email);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentotp = pinView.getText().toString();
                 if(sendopt.equals(currentotp)){
                     Intent intent = new Intent(OTP_enter.this, Change_password.class);
                     intent.putExtra("email",email);
                     intent.putExtra("docId",docId);
                     startActivity(intent);
                     finish();

                 }else{
//                if otp wrong
                MysalonListPopup();

                 }

            }
        });

    }


    public void MysalonListPopup() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View popAlert = getLayoutInflater().inflate(R.layout.otp_wrong_pop, null);

        Button try_btn = popAlert.findViewById(R.id.otp_try_btn);
        Button re_btn = popAlert.findViewById(R.id.resend_otp_btn);
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
                Intent intent = new Intent(OTP_enter.this, Forget_password.class);
                startActivity(intent);
                finish();
            }
        });


    }
}