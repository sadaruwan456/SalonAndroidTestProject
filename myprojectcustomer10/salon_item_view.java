package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dilanka456.myprojectcustomer10.Holder.SalonServiceListHolder;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class salon_item_view extends AppCompatActivity {

    ImageView salon_img;
    TextView salon_name_fld, salon_address_fld,go_direction_lbl,salon_mobile_fld;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String salonDocId;

    RecyclerView packages_list;
    FirestoreRecyclerAdapter<Package, SalonServiceListHolder> pack_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_item_view);

        Bundle extras = getIntent().getExtras();
        salonDocId = extras.getString("SalonDocId");


//        ------------------------------assign Fields---------------------------
        salon_img = findViewById(R.id.salon_item_image_view);
        salon_name_fld = findViewById(R.id.salon_item_name_fld);
        salon_address_fld = findViewById(R.id.salon_item_address_fld);
        salon_mobile_fld = findViewById(R.id.salon_item_mobile_fld);

        packages_list = findViewById(R.id.salon_packages_list_view);
        go_direction_lbl = findViewById(R.id.get_directopn_lbl);




//        --------------------------------get Salon-------------------------------
        db.collection("Salon").document(salonDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Salon salon = documentSnapshot.toObject(Salon.class);
                    Picasso.get().load(salon.getLogo_path()).into(salon_img);
                    salon_name_fld.setText(salon.getName());
                    salon_address_fld.setText(salon.getAddress());
                    salon_mobile_fld.setText(salon.getContact());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


//-------------------------------------load Packages---------------------------------
        Query query = db.collection("Package").whereEqualTo("salon_doc_id", salonDocId);

        FirestoreRecyclerOptions<Package> options = new FirestoreRecyclerOptions.Builder<Package>().setQuery(query, Package.class).build();
        packages_list.setLayoutManager(new LinearLayoutManager(this));
        pack_adapter = new FirestoreRecyclerAdapter<Package, SalonServiceListHolder>(options) {
            @NonNull
            @Override
            public SalonServiceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.salon_service_list_item, parent, false);
                return new SalonServiceListHolder(inflate);
            }

            @Override
            protected void onBindViewHolder(@NonNull SalonServiceListHolder holder, int position, @NonNull Package model) {
                holder.pack_name.setText(model.getName());
                holder.pack_price.setText("Rs." + model.getPrice());
                holder.pack_duration.setText(model.getDuration() + "Min");
                Picasso.get().load(model.getImg_url()).into(holder.pack_img);
                String PackageDocId = getSnapshots().getSnapshot(position).getId();
                holder.pack_doc_id = PackageDocId;

            }
        };
        packages_list.setAdapter(pack_adapter);



        go_direction_lbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(salon_item_view.this,Salon_direction.class);
                intent.putExtra("SalonDocId",salonDocId);
                startActivity(intent);
            }
        });

//----------------------------------End OnCreate-----------------------
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (pack_adapter!=null){
            pack_adapter.startListening();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pack_adapter!=null){
            pack_adapter.stopListening();

        }
    }
}