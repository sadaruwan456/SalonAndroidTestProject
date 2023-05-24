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

public class AppRequestListHolder extends RecyclerView.ViewHolder {

    private final FCMClient fcmClient;
    private final View view;
    public TextView package_name,package_price,appointment_date,appointment_time,customer_name;
    public ImageView package_img;
    public Button appoint_accept_btn,appoint_reject_btn;
    public String appointmentDocID;
    public  String customerDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String fcm_token;

    public AppRequestListHolder(View view){
        super(view);
        this.view = view;


        fcmClient = new FCMClient();

        package_name = view.findViewById(R.id.app_pack_name_fld);
        package_price = view.findViewById(R.id.app_pack_total_fld);
        package_img = view.findViewById(R.id.app_pack_img);
        appointment_date = view.findViewById(R.id.app_pack_date_fld);
        appointment_time = view.findViewById(R.id.app_pack_time_fld);
        appoint_accept_btn = view.findViewById(R.id.app_accept_btn);
        appoint_reject_btn = view.findViewById(R.id.app_reject_btn);
        customer_name = view.findViewById(R.id.app_customer_name_fld);

        appoint_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Appointment").document(appointmentDocID).update("status","Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        sentNotify("Your Appointment has been accepted");
                        Toast.makeText(view.getContext(), "Appointment Accepted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });

        appoint_reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Appointment").document(appointmentDocID).update("status","Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(view.getContext(), "Appointment Rejected", Toast.LENGTH_SHORT).show();
                        sentNotify("Your Appointment has been rejected");
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
