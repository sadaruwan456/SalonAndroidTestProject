package com.dilanka456.myprojectsalonapp10;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dilanka456.myprojectsalonapp10.Model.Salon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class MySalon extends AppCompatActivity {

    private static final String TAG = "MysalonTag";
    private String name;
    private String email;
    private String uid;
    private String owner_docId;

    TextView salon_name,salon_slogan,salon_email,salon_address,salon_mobile,delete_salon_btn;
    ImageView back_btn,logo_view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String salon_doc_id;
    private AlertDialog.Builder dialogBuilder,emtdialogBuilder;
    private AlertDialog alertDialog,emtalertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_salon);

//        -------------------------------------Assign Fileds--------------------------------------------
        salon_name = findViewById(R.id.mysalon_salon_name_lbl);
        salon_slogan = findViewById(R.id.mysalon_slogan_lbl);
        salon_email = findViewById(R.id.mysalon_email_lbl);
        salon_address = findViewById(R.id.mysalon_address_lbl);
        salon_mobile = findViewById(R.id.mysalon_mobile_lbl);

        delete_salon_btn = findViewById(R.id.mysalon_delete_salon_btn);
        back_btn = findViewById(R.id.my_salon_back_btn);
        logo_view = findViewById(R.id.mysalon_logo_view);

//        ----------------------------------------back_btn action------------------------------
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


//        --------------------------------Existing Data---------------------------------------------
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        email = extras.getString("email");
        uid = extras.getString("uid");
        owner_docId = extras.getString("owner_docId");
        salon_doc_id = extras.getString("salon_doc_id");


        db.collection("Salon").document(salon_doc_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Salon salon = documentSnapshot.toObject(Salon.class);
                    salon_name.setText(salon.getName());
                    salon_slogan.setText(salon.getSlogan());
                    salon_email.setText(salon.getEmail());
                    salon_address.setText(salon.getAddress());
                    salon_mobile.setText(salon.getContact());

                    Picasso.get().load(salon.getLogo_path()).into(logo_view);
                }else{

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


//        ---------------------------------------------------delete salon Action---------------------------------------------
        delete_salon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //salon delete code
                MysalonListPopup();

            }
        });

    }

    public void MysalonListPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View myListVIew = getLayoutInflater().inflate(R.layout.mysalon_delete_popup,null);
        dialogBuilder.setView(myListVIew);
        alertDialog = dialogBuilder.create();
        alertDialog.show();


        Button cancel_btn = myListVIew.findViewById(R.id.delete_pop_cancel_btn);
        Button ok_btn = myListVIew.findViewById(R.id.delete_pop_ok_btn);


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Package").whereEqualTo("salon_doc_id",salon_doc_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size()>0){

                            packagesDeletePop();

                        }else{

                            deleteSalonOk();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Error: "+e.getMessage());
                    }
                });


            }
        });



    }

    public void packagesDeletePop() {
        emtdialogBuilder = new AlertDialog.Builder(this);
        final View myListVIew = getLayoutInflater().inflate(R.layout.empty_pop,null);

        ImageView close_btn = myListVIew.findViewById(R.id.empty_pop_close_btn);
        TextView header_lbl = myListVIew.findViewById(R.id.empty_pop_header_lbl);

        header_lbl.setText("Please delete your Packages first...!");

        emtdialogBuilder.setView(myListVIew);
        emtalertDialog = emtdialogBuilder.create();
        emtalertDialog.show();

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emtalertDialog.cancel();
            }
        });


    }

    private void deleteSalonOk() {
        db.collection("Salon").document(salon_doc_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(MySalon.this, Home.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}