package com.dilanka456.myprojectcustomer10.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dilanka456.myprojectcustomer10.Holder.DiscountListHolder;
import com.dilanka456.myprojectcustomer10.Model.Discount;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.dilanka456.myprojectcustomer10.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DiscountListAdapter extends FirestoreRecyclerAdapter<Discount, DiscountListHolder> {

    private static final String TAG = "disList";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DiscountListAdapter(@NonNull FirestoreRecyclerOptions<Discount> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull DiscountListHolder holder, int position, @NonNull Discount model) {

        db.collection("Package").document(model.getPackage_doc_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Package packageModelOb = documentSnapshot.toObject(Package.class);
                    holder.dis_pack_name.setText(packageModelOb.getName());
                    holder.dis_pack_price.setText("Rs."+packageModelOb.getPrice());
                    holder.dis_pack_dis.setText(model.getDiscount()+"% Off");
                    Picasso.get().load(packageModelOb.getImg_url()).into(holder.dis_pack_img);
                    holder.pack_doc_id = documentSnapshot.getId();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"disLoadError: "+e.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public DiscountListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_list_item, parent, false);
        return new DiscountListHolder(view);
    }
}
