package com.dilanka456.myprojectsalonapp10.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dilanka456.myprojectsalonapp10.Holders.AppRequestListHolder;
import com.dilanka456.myprojectsalonapp10.Model.Appointment;
import com.dilanka456.myprojectsalonapp10.Model.Customer;
import com.dilanka456.myprojectsalonapp10.Model.Package;
import com.dilanka456.myprojectsalonapp10.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AppRequstListAdapter extends FirestoreRecyclerAdapter<Appointment, AppRequestListHolder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AppRequstListAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AppRequestListHolder holder, int position, @NonNull Appointment model) {


        holder.package_price.setText("Rs."+model.getTotalPrice());
        holder.appointment_date.setText(model.getDate());
        holder.appointment_time.setText(model.getTime());
        String appointId = getSnapshots().getSnapshot(position).getId();
        holder.appointmentDocID = appointId;
        holder.customerDocId = model.getCustomerDocId();

        db.collection("Package").document(model.getPackageDocId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Package aPackage = documentSnapshot.toObject(Package.class);
                    Picasso.get().load(aPackage.getImg_url()).into(holder.package_img);
                    holder.package_name.setText(aPackage.getName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        db.collection("Customer").document(model.getCustomerDocId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    holder.customer_name.setText(customer.getName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        }

    @NonNull
    @Override
    public AppRequestListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_request_list_item, parent, false);
        return new AppRequestListHolder(view);
    }
}
