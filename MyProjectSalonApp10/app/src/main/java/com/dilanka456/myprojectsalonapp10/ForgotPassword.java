package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dilanka456.myprojectsalonapp10.AsyncTasks.OtpSaveTask;
import com.dilanka456.myprojectsalonapp10.Utils.OTPgen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "forgot_pw_tag";
    ImageView back_btn;
    Button next_btn;
    EditText email_fld;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        next_btn = findViewById(R.id.next_email_send_btn);
        back_btn = findViewById(R.id.back_btn_forgot_pw);
        email_fld = findViewById(R.id.otp_send_email_fld);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                char[] chars = new OTPgen().generatorOTP(6);
                int otpNumber = Integer.parseInt(new String(chars));
                String otp = String.valueOf(otpNumber);
                Log.d(TAG,"OTP"+otpNumber);

                db.collection("Owner").whereEqualTo("email",email_fld.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size()>0){
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String ownerDocIt = documentSnapshot.getId();
                            new OtpSaveTask(v).execute(email_fld.getText().toString(), otp,ownerDocIt);

                        }else{

                            Toast.makeText(ForgotPassword.this, "Wrong Email Enter Registered Email", Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"Error:"+e.getMessage());
                    }
                });




            }
        });
    }
}