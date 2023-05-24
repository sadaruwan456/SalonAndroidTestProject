package com.dilanka456.myprojectsalonapp10;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dilanka456.myprojectsalonapp10.Model.Discount;
import com.dilanka456.myprojectsalonapp10.Model.Package;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Create_discount extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText pack_name_fld,pack_price_fld,discount_fld;
    Button add_dic_btn,remove_dic_btn;
    private String salonDocId;
    private String packageId;
    private String DiscountDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discount);

        pack_name_fld = findViewById(R.id.pack_name_discount);
        pack_price_fld = findViewById(R.id.pack_price_discount);
        discount_fld = findViewById(R.id.pack_discount_fld);
        add_dic_btn = findViewById(R.id.add_dic_btn);
        remove_dic_btn = findViewById(R.id.remove_dic_btn);


        Bundle extras = getIntent().getExtras();
         salonDocId = extras.getString("salonDocId");
         packageId = extras.getString("packageId");

        db.collection("Package").document(packageId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Package aPackage = documentSnapshot.toObject(Package.class);
                    pack_name_fld.setText(aPackage.getName());
                    pack_price_fld.setText(aPackage.getPrice());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Create_discount.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        db.collection("Discount").whereEqualTo("package_doc_id",packageId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size()>0){
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                   DiscountDocId = documentSnapshot.getId();
                    Discount discount = documentSnapshot.toObject(Discount.class);
                    discount_fld.setText(discount.getDiscount());
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        add_dic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discount discount = new Discount();
                discount.setPackage_doc_id(packageId);
                discount.setSalon_doc_id(salonDocId);
                discount.setDiscount(discount_fld.getText().toString());

                db.collection("Discount").add(discount).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Create_discount.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        remove_dic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Discount").document(DiscountDocId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Create_discount.this, "Discount Removed", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Create_discount.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}