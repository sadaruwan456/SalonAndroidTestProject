package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dilanka456.myprojectcustomer10.Model.Customer;
import com.dilanka456.myprojectcustomer10.Validation.Validation;
import com.firebase.ui.auth.ui.idp.SingleSignInActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignIn extends AppCompatActivity {
    private static final int FILE_CHOOSE_ACTIVITY_RESULT_CODE = 100;
    private static final String TAG = "SignIn Tag :";
    EditText name_fld, mobile_fld, email_fld, password_fld, password_re_fld;
    Button regi_btn;
    CheckBox showBox;
    ImageView pro_pic_view;
    private Uri selectedProductImageURI;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String customer_pro_pic_Url;
    private StorageReference storageReference;
    private String name_txt;
    private String downloadImageUrl;
    private String downUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        storageReference = FirebaseStorage.getInstance().getReference();
//        ----------------------------------------------Assign Fields-------------------------------------
        name_fld = findViewById(R.id.signin_name_fld);
        mobile_fld = findViewById(R.id.signin_mobile_fld);
        email_fld = findViewById(R.id.signin_email_fld);
        password_fld = findViewById(R.id.signin_password_fld);
        password_re_fld = findViewById(R.id.signin_password_re_fld);


        regi_btn = findViewById(R.id.signin_register_btn);
        showBox = findViewById(R.id.signin_showBox);
        pro_pic_view = findViewById(R.id.signin_pro_pic_view);

//        ---------------------------------get data from login-----------------------------------------------
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("name") != null) {
            name_txt = bundle.getString("name");

        }

        String email_txt = bundle.getString("email");
        String uid_txt = bundle.getString("uid");


//        ------------------------------------------set existing data for fields-------------------------------------
        name_fld.setText(name_txt);
        email_fld.setText(email_txt);


        //        ------------------------------------------check box design code---------------------------------------------------------------
        showBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password_fld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_re_fld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password_fld.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_re_fld.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
//        ------------------------------------end check box design code-----------------------------------------------------------


//        ------------------------------------------------Profile Pic select and get it---------------------------------------------

        pro_pic_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileChooser = new Intent();
                fileChooser.setAction(Intent.ACTION_GET_CONTENT);
                fileChooser.setType("image/*"); // optional
                startActivityForResult(Intent.createChooser(fileChooser, "Select File"), FILE_CHOOSE_ACTIVITY_RESULT_CODE);
            }
        });


//        -----------------------------------------------Register Button Event--------------------------------
        regi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valideFields = validateFields();
                if (valideFields) {

                    if (password_fld.getText().toString().equals(password_re_fld.getText().toString())) {

                        uploadProfilePic();

                    }else{
                        password_re_fld.setBackgroundResource(R.drawable.text_fields_data_wrong_style);
                    }

                }
            }
        });

//        ---------------------------------------------End of onCreate-----------------------------------
    }


    //     -------------------------------------------------Profile Pic Upload Method----------------------------------
    private void uploadProfilePic() {
        customer_pro_pic_Url = "customer_profilePic/" + name_fld.getText().toString() + System.currentTimeMillis() + ".png";
        StorageReference filePath = storageReference.child(customer_pro_pic_Url);

        final UploadTask uploadTask = filePath.putFile(selectedProductImageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "ProPic Upload Error: "+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw  task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downUrl = task.getResult().toString();

                        Customer customer = new Customer();
                        customer.setName(name_fld.getText().toString());
                        customer.setMobile(mobile_fld.getText().toString());
                        customer.setEmail(email_fld.getText().toString());
                        customer.setPassword(password_fld.getText().toString());
                        customer.setPro_pic_Url(downUrl);

                        db.collection("Customer").add(customer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Customer Save Successfully");

                                Toast.makeText(SignIn.this, "Customer Saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignIn.this, Login_activity.class);
//                                intent.putExtra("name", name_fld.getText().toString());
//                                intent.putExtra("email", email_fld.getText().toString());
//                                intent.putExtra("pro_url",downUrl);
                                startActivity(intent);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Customer Save Error :" + e.getMessage());

                            }
                        });


                    }
                });

            }
        });



//        storageReference.child(customer_pro_pic_Url).
//                putFile(selectedProductImageURI).
//                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Log.d(TAG, "Profile Pic Uploaded");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Profile Pic Upload Error:" + e.getMessage());
//
//            }
//        });
    }


    //    -----------------------------------------------Activity Result Method------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FILE_CHOOSE_ACTIVITY_RESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                selectedProductImageURI = data.getData();

                Picasso.get().load(selectedProductImageURI).into(pro_pic_view);

            } else {
                Toast.makeText(this, "File not Selected : " + resultCode, Toast.LENGTH_SHORT).show();
            }
        }

    }

    //    -----------------------------------------------END of Activity Result Method------------------------------------------------------

    public boolean validateFields() {

        boolean valideOk = false;

        boolean validateEmail = new Validation().validateEmail(email_fld.getText().toString());

        if (validateEmail) {
//            Valid Email

            valideOk = true;
        } else {
//            Invalid Email
            email_fld.setBackgroundResource(R.drawable.text_fields_data_wrong_style);
            valideOk = false;
        }

        return valideOk;
    }


}