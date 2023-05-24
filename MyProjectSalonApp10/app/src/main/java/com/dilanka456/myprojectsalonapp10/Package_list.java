package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dilanka456.myprojectsalonapp10.Holders.PackageListHolader;
import com.dilanka456.myprojectsalonapp10.Model.Discount;
import com.dilanka456.myprojectsalonapp10.Model.Package;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class Package_list extends AppCompatActivity {

    RecyclerView pack_list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<Package, PackageListHolader> recyclerAdapter;
    private String salonDocId;
    Button create_package_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);

        Bundle bundle = getIntent().getExtras();
         salonDocId = bundle.getString("salonDocId");



         create_package_btn = findViewById(R.id.create_new_pack_btn);

//        ********meya salon doc id eken restrict karanna oni********************
        Query query = db.collection("Package").whereEqualTo("salon_doc_id",salonDocId);
        FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<Package>().setQuery(query, Package.class).build();

        pack_list = findViewById(R.id.package_list_review);
        pack_list.setLayoutManager(new LinearLayoutManager(this));

        recyclerAdapter = new FirestoreRecyclerAdapter<Package, PackageListHolader>(recyclerOptions) {
            @NonNull
            @Override
            public PackageListHolader onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item_layout, parent, false);

                return new PackageListHolader(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PackageListHolader holder, int position, @NonNull Package model) {

//                Toast.makeText(Package_list.this, "name"+model.getName(), Toast.LENGTH_SHORT).show();

                holder.pack_name.setText(model.getName());
                holder.pack_des.setText(model.getDescription());
                holder.pack_price.setText("Rs."+model.getPrice());
                Picasso.get().load(model.getImg_url()).into(holder.pack_img);
                String packageId = getSnapshots().getSnapshot(position).getId();
                holder.packageId = packageId;
                holder.salon_doc_id = salonDocId;
                holder.package_new = model;

                db.collection("Discount").whereEqualTo("package_doc_id",packageId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size()>0){
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Discount discount = documentSnapshot.toObject(Discount.class);
                            holder.dic_lbl.setVisibility(View.VISIBLE);
                            holder.dic_lbl.setText(discount.getDiscount()+"% Off");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        };

        pack_list.setAdapter(recyclerAdapter);


        create_package_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Package_list.this,Create_packages.class);
                intent.putExtra("salonDocId",salonDocId);
                intent.putExtra("packFound",false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}