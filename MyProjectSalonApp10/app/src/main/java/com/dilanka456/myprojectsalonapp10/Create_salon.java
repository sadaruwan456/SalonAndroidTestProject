package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dilanka456.myprojectsalonapp10.Model.Owner;
import com.dilanka456.myprojectsalonapp10.Model.Salon;
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

public class Create_salon extends AppCompatActivity {
    private static final int FILE_CHOOSE_SALON_LOGO_ACTIVITY_RESULT_CODE = 120;
    private static final String TAG = "CreateSalon";
    EditText salon_name, slogan_fld, salon_contact, salon_email, salon_address;
    ImageView logo_pic_view;
    Button next_btn;
    private Uri selectedLogoImageURI;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private String name;
    private String email;
    private String uid;
    private String owner_docId;
    private String downloadImageUrl;
    private String downUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_salon);

        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        email = extras.getString("email");
        uid = extras.getString("uid");
        owner_docId = extras.getString("owner_docId");


//        Assign Fields
        salon_name = findViewById(R.id.signin_salon_name_fld);
        slogan_fld = findViewById(R.id.signin_salon_slogan_fld);
        salon_contact = findViewById(R.id.signin_salon_contact_number_fld);
        salon_email = findViewById(R.id.signin_salon_email_fld);
        salon_address = findViewById(R.id.signin_salon_address_fld);

        next_btn = findViewById(R.id.create_salon_nest_btn);
        logo_pic_view = findViewById(R.id.signin_salon_logo_pic_fld);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadLogo();

            }
        });



        logo_pic_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileChooser = new Intent();
                fileChooser.setAction(Intent.ACTION_GET_CONTENT);
                fileChooser.setType("image/*"); // optional
                startActivityForResult(Intent.createChooser(fileChooser, "Select File"), FILE_CHOOSE_SALON_LOGO_ACTIVITY_RESULT_CODE);
            }
        });
    }

    private void uploadLogo() {

        String salon_logo_path = "Salon_Logos/" + salon_name.getText().toString() + System.currentTimeMillis() + ".png";

        StorageReference filePath = storageReference.child(salon_logo_path);

        final UploadTask uploadTask = filePath.putFile(selectedLogoImageURI);

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
                        Salon salon = new Salon();
                        salon.setName(salon_name.getText().toString());
                        salon.setSlogan(slogan_fld.getText().toString());
                        salon.setContact(salon_contact.getText().toString());
                        salon.setEmail(salon_email.getText().toString());
                        salon.setAddress(salon_address.getText().toString());
                        salon.setOwner_docId(owner_docId);
                        salon.setLogo_path(downUrl);


                        db.collection("Salon").add(salon).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Salon_Saved");

                                updateSalon(documentReference);

                                Intent intent = new Intent(Create_salon.this, Location_set_activity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("email",email);
                                intent.putExtra("uid",uid);
                                intent.putExtra("owner_docId",owner_docId);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Salon_Save Error:" + e.getMessage());

                            }
                        });



                    }
                });

            }
        });

//        storageReference.child(logoPicPath).
//                putFile(selectedLogoImageURI).
//                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Log.d(TAG, "Logo Uploaded");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Logo Upload Error: " + e.getMessage());
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Logo Picture get and set to image view
        if (FILE_CHOOSE_SALON_LOGO_ACTIVITY_RESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                selectedLogoImageURI = data.getData();

                Picasso.get().load(selectedLogoImageURI).into(logo_pic_view);

            } else {
                Toast.makeText(this, "Logo File not Selected : " + resultCode, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updateSalon(DocumentReference documentReference) {

        documentReference.update("owner_docId",owner_docId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Salon_updated");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Salon_update_Error: "+e.getMessage());

            }
        });


    }
}