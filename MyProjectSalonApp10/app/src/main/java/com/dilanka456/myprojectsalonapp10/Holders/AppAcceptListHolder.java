package com.dilanka456.myprojectsalonapp10.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectsalonapp10.Model.Customer;
import com.dilanka456.myprojectsalonapp10.R;
import com.dilanka456.myprojectsalonapp10.fcmHelper.FCMClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppAcceptListHolder extends RecyclerView.ViewHolder {
    private final FCMClient fcmClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ImageView acc_img;
    public TextView acc_name,acc_price,acc_date,acc_time,acc_customer_name;
    public Button acc_done_btn;
    public String appointmentDocId;
    public String customerDocId;
    private String fcm_token;

    public AppAcceptListHolder(View viewItem){
        super(viewItem);
        fcmClient = new FCMClient();
        acc_img = viewItem.findViewById(R.id.app_acc_img);
        acc_name = viewItem.findViewById(R.id.app_acc_name);
        acc_price = viewItem.findViewById(R.id.app_acc_price);
        acc_date = viewItem.findViewById(R.id.app_acc_date);
        acc_time = viewItem.findViewById(R.id.app_acc_time);
        acc_customer_name = viewItem.findViewById(R.id.app_acc_customer_name);
        acc_done_btn = viewItem.findViewById(R.id.app_acc_done_btn);

        acc_done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Appointment").document(appointmentDocId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(viewItem.getContext(), "Appointment Finished", Toast.LENGTH_SHORT).show();

                        sentNotify("Your Appointment is Finished, Thank you...!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

    }

    public void sentNotify(String msg){
//        ------------------------get FVM Token-------------------------

        db.collection("Customer").document(customerDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    fcm_token = customer.getFcm_token();
                    fcmClient.execute(fcm_token, "Appointment Status",msg);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }


}
