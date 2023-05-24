package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dilanka456.myprojectsalonapp10.Model.Salon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Location_set_activity extends AppCompatActivity {

    private static final String TAG = "Location_Set_TAG";
    private LatLng customerLocation;
    Button locationSaveBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email;
    private String name;
    private String uid;
    private String owner_docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_set_activity);


        Bundle bundle = getIntent().getExtras();
         email = bundle.getString("email");
         name = bundle.getString("name");
         uid = bundle.getString("uid");
         owner_docId = bundle.getString("owner_docId");

        locationSaveBtn = findViewById(R.id.location_save_btn);
        locationSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Salon").whereEqualTo("owner_docId",owner_docId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.toObjects(Salon.class).size()>0){
                            String salon_doc_Id = queryDocumentSnapshots.getDocuments().get(0).getId();
                            UpdateLocation(salon_doc_Id);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


    }

    private void UpdateLocation(String salon_doc_id) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("location_lat",customerLocation.latitude);
        childUpdates.put("location_lon",customerLocation.longitude);

        db.collection("Salon").document(salon_doc_id).update(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG,"Location_Updated");

                Intent intent = new Intent(Location_set_activity.this,Home.class);
                intent.putExtra("name",name);
                intent.putExtra("email", email);
                intent.putExtra("uid",uid);
                intent.putExtra("owner_docId", owner_docId);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Update_Error: "+e.getMessage());

            }
        });
    }

    public void setCurrentLatLon(LatLng customerLocation) {
        this.customerLocation = customerLocation;

    }

}