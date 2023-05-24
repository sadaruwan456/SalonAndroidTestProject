package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Salon_direction extends AppCompatActivity {


    private String salonDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private double salonLocation_lat;
    private double salonLocation_lon;

    TextView salon_doc_test_lat_lbl,salon_doc_test_lon_lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_direction);

        salon_doc_test_lat_lbl = findViewById(R.id.salon_doc_test_lat_lbl);
        salon_doc_test_lon_lbl = findViewById(R.id.salon_doc_test_lon_lbl);


//        ---------------------------get Existing Data-----------------------
        Bundle extras = getIntent().getExtras();
        salonDocId = extras.getString("SalonDocId");


        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        MapsFragment mapsFragment = new MapsFragment();
        fragmentTransaction.add(R.id.map_load_container,mapsFragment,"direction_map");
        fragmentTransaction.commit();


        db.collection("Salon").document(salonDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Salon salon = documentSnapshot.toObject(Salon.class);

                    salon_doc_test_lat_lbl.setText(String.valueOf(salon.getLocation_lat()));
                    salon_doc_test_lon_lbl.setText(String.valueOf(salon.getLocation_lon()));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

}