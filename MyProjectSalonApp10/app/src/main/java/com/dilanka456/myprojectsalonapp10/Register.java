package com.dilanka456.myprojectsalonapp10;

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

import com.dilanka456.myprojectsalonapp10.Model.Owner;
import com.dilanka456.myprojectsalonapp10.Model.Salon;
import com.dilanka456.myprojectsalonapp10.Validation.Validation;
import com.firebase.ui.auth.ui.InvisibleActivityBase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final int FILE_CHOOSE_PROFILE_PIC_ACTIVITY_RESULT_CODE = 100;

    private static final String TAG = "Register_tag";
    EditText owner_name, owner_mobile, owner_email, owner_address, password_fld, password_re_fld;
    ImageView pro_pic_view;
    Button next_btn;
    CheckBox show_box;
    private Uri selectedProfileImageURI;

    private String name;
    private String email;
    private String uid;
    private StorageReference storageReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String owner_doc_id;
    private Validation validation;
    private String downUrl;
    private CollectionReference ownerCollection;
    private String downloadImageUrl;
    private boolean myaccountFound;
    private String ownerDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        storageReference = FirebaseStorage.getInstance().getReference();
        ownerCollection = db.collection("Owner");

//        ------------------------------Get Existing Data--------------------------------------------
        uid = "Login_Manual";
        name = " ";
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");

        if (extras.getString("uid") != null) {

            name = extras.getString("name");
            uid = extras.getString("uid");
        }


//        --------------------------Field Assign----------------------------------------------------
        //Owner fields
        owner_name = findViewById(R.id.signin_owner_name_fld);
        owner_mobile = findViewById(R.id.signin_owner_mobile_fld);
        owner_email = findViewById(R.id.signin_owner_email_fld);
        owner_address = findViewById(R.id.signin_owner_address_fld);
        pro_pic_view = findViewById(R.id.signin_owner_pro_pic_fld);

        //security field
        password_fld = findViewById(R.id.signin_owner_password_fld);
        password_re_fld = findViewById(R.id.signin_owner_con_password_fld);
        show_box = findViewById(R.id.signin_owner_password_show_box_fld);
        next_btn = findViewById(R.id.signin_next_btn);

        if (extras.get("MyaccountFound")!=null) {
            myaccountFound = (boolean) extras.get("MyaccountFound");
            ownerDocId = extras.getString("OwnerDocId");

            next_btn.setText("Update Owner");
            db.collection("Owner").document(ownerDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Owner owner = documentSnapshot.toObject(Owner.class);
                        owner_name.setText(owner.getName());
                        owner_email.setText(owner.getEmail());
                        owner_address.setText(owner.getAddress());
                        owner_mobile.setText(owner.getMobile());
                        Picasso.get().load(owner.getPro_pic_url()).into(pro_pic_view);
                        password_fld.setText(owner.getPassword());
                        password_re_fld.setText(owner.getPassword());

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        } else {
            myaccountFound = false;

            owner_name.setText(name);
            owner_email.setText(email);

        }
        //Salon fields


        //        ------------------------------------------check box design code---------------------------------------------------------------
        show_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


        pro_pic_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileChooser = new Intent();
                fileChooser.setAction(Intent.ACTION_GET_CONTENT);
                fileChooser.setType("image/*"); // optional
                startActivityForResult(Intent.createChooser(fileChooser, "Select File"), FILE_CHOOSE_PROFILE_PIC_ACTIVITY_RESULT_CODE);
            }
        });


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validateFields = validateFields();
                if (validateFields) {

                    if (password_fld.getText().toString().equals(password_re_fld.getText().toString())){

                    picturesUpload();

                    }else{
                        password_fld.setBackgroundResource(R.drawable.text_fields_data_wrong_style);
                        password_re_fld.setBackgroundResource(R.drawable.text_fields_data_wrong_style);
                    }

                }


//                -------------------------End fire base data update-----------------------

//                *************Database ekata data save karana part eka karanna********************************
            }

        });

    }


    private void picturesUpload() {


        String pro_pic_path = "Profile_pics/" + owner_name.getText().toString() + "_" + System.currentTimeMillis() + ".png";

        StorageReference filePath = storageReference.child(pro_pic_path);

        final UploadTask uploadTask = filePath.putFile(selectedProfileImageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "ProPic Upload Error: " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downUrl = task.getResult().toString();
                        Owner owner = new Owner();
                        owner.setName(owner_name.getText().toString());
                        owner.setMobile(owner_mobile.getText().toString());
                        owner.setEmail(owner_email.getText().toString());
                        owner.setAddress(owner_address.getText().toString());
                        owner.setPassword(password_fld.getText().toString());
                        owner.setStatus("Active");
                        owner.setAuth_id(uid);
                        owner.setPro_pic_url(downUrl);

                        if (myaccountFound) {

                            Map<String,Object> map = new HashMap<>();
                            map.put("name",owner_name.getText().toString());
                            map.put("mobile",owner_mobile.getText().toString());
                            map.put("email",owner_email.getText().toString());
                            map.put("address",owner_address.getText().toString());
                            map.put("password", password_fld.getText().toString());
                            map.put("status","Active");
                            map.put("pro_pic_url",downUrl);

                            ownerCollection.document(ownerDocId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    onBackPressed();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        } else {


                            ownerCollection.add(owner).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    owner_doc_id = documentReference.getId();
                                    Log.d(TAG, "Owner_Saved");
                                    Intent intent = new Intent(Register.this, Login.class);
//                                    intent.putExtra("name", name);
//                                    intent.putExtra("email", email);
//                                    intent.putExtra("uid", uid);
//                                    intent.putExtra("owner_docId", owner_doc_id);
                                    startActivity(intent);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Owner_Save Error:" + e.getMessage());
                                }
                            });

                        }
                    }
                });

            }
        });


//        storageReference.child(proPicUrl).
//                putFile(selectedProfileImageURI).
//                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Log.d(TAG, "Profile Pic Uploaded");
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "ProPic Upload Error: "+e.getMessage());
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Profile Picture get and set to image view
        if (FILE_CHOOSE_PROFILE_PIC_ACTIVITY_RESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                selectedProfileImageURI = data.getData();

                Picasso.get().load(selectedProfileImageURI).into(pro_pic_view);

            } else {
                Toast.makeText(this, "Pro Pic File not Selected : " + resultCode, Toast.LENGTH_SHORT).show();
            }
        }


    }


    public boolean validateFields() {

        boolean valideOk = false;

        boolean validateEmail = new Validation().validateEmail(owner_email.getText().toString());

        if (validateEmail) {
//            Valid Email

            valideOk = true;
        } else {
//            Invalid Email
            owner_email.setBackgroundResource(R.drawable.text_fields_data_wrong_style);
            valideOk = false;
        }

        return valideOk;
    }

}