package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.dilanka456.myprojectcustomer10.Model.Customer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Edite_profile extends AppCompatActivity {

    EditText pro_name,pro_email,pro_mobile,pro_password,pro_password_re;
    ImageView pro_pic_view;
    Button pro_update_btn;
    CheckBox showBox;
    private String customerDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);

        Bundle extras = getIntent().getExtras();
        customerDocId = extras.getString("CustomerDocId");

        pro_name = findViewById(R.id.pro_name_fld);
        pro_email = findViewById(R.id.pro_email_fld);
        pro_mobile = findViewById(R.id.pro_mobile_fld);
        pro_password=  findViewById(R.id.pro_password_fld);
        pro_password_re = findViewById(R.id.pro_password_re_fld);
        pro_pic_view = findViewById(R.id.pro_pro_pic_view);
        pro_update_btn = findViewById(R.id.pro_update_btn);
        showBox = findViewById(R.id.pro_showBox);


        //        ------------------------------------------check box design code---------------------------------------------------------------
        showBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pro_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pro_password_re.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pro_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pro_password_re.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });


        db.collection("Customer").document(customerDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    pro_name.setText(customer.getName());
                    pro_email.setText(customer.getEmail());
                    pro_mobile.setText(customer.getMobile());
                    pro_password.setText(customer.getPassword());
                    Picasso.get().load(customer.getPro_pic_Url()).into(pro_pic_view);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        pro_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}