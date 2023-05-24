package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dilanka456.myprojectsalonapp10.Model.Salon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Edite_salon extends AppCompatActivity {

    EditText salon_name_fld, salon_slogan_fld, salon_contact_fld, salon_email_fld, salon_address_fld;
    ImageView logo_img;
    Button next_btn;
    private String owner_docId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String salon_doc_id;
    private String uid;
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_salon);

//        -------------------------------------------Assign Feilds------------------------------------
        salon_name_fld = findViewById(R.id.edit_salon_name);
        salon_slogan_fld = findViewById(R.id.edit_slogan);
        salon_contact_fld = findViewById(R.id.edit_contact);
        salon_email_fld = findViewById(R.id.edit_email);
        salon_address_fld = findViewById(R.id.edit_address);
        logo_img = findViewById(R.id.edit_logo_view);
        next_btn = findViewById(R.id.edit_next_btn);


//        -----------------------------------get existing data-----------------------------------------
        Bundle extras = getIntent().getExtras();
        owner_docId = extras.getString("owner_docId");
        uid = extras.getString("uid");
        name = extras.getString("name");
        email = extras.getString("email");

        db.collection("Salon").whereEqualTo("owner_docId", owner_docId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    salon_doc_id = documentSnapshot.getId();
                    Salon salon = documentSnapshot.toObject(Salon.class);
                    salon_name_fld.setText(salon.getName());
                    salon_slogan_fld.setText(salon.getSlogan());
                    salon_contact_fld.setText(salon.getContact());
                    salon_email_fld.setText(salon.getEmail());
                    salon_address_fld.setText(salon.getAddress());

                    Picasso.get().load(salon.getLogo_path()).into(logo_img);

                } else {


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", salon_name_fld.getText().toString());
                map.put("slogan", salon_slogan_fld.getText().toString());
                map.put("contact", salon_contact_fld.getText().toString());
                map.put("email", salon_email_fld.getText().toString());
                map.put("address", salon_address_fld.getText().toString());


                db.collection("Salon").document(salon_doc_id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(Edite_salon.this, Location_set_activity.class);
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

                    }
                });
            }
        });

    }
}