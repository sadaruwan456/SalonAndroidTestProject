package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Change_password extends AppCompatActivity {

    private static final String TAG = "Change_password_tag";
    EditText password_fld, getPassword_re_fld;
    Button save_btn;
    CheckBox showBox;
    TextView error_txt;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String docId = bundle.getString("docId");

        password_fld = findViewById(R.id.new_password_fld);
        getPassword_re_fld = findViewById(R.id.new_re_password_fld);
        save_btn = findViewById(R.id.new_password_save_btn);
        showBox = findViewById(R.id.new_password_show_box);
        error_txt = findViewById(R.id.error_txt);

        //        ------------------------------------------check box design code---------------------------------------------------------------
        showBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    password_fld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    getPassword_re_fld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password_fld.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    getPassword_re_fld.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password_fld.getText().toString().equals(getPassword_re_fld.getText().toString())){


                db.collection("Owner").document(docId).update("password", password_fld.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(Change_password.this,Login.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"Error:"+e.getMessage());
                    }
                });

                }else{

                    error_txt.setVisibility(View.VISIBLE);

                }

            }
        });


    }
}