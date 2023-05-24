package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dilanka456.myprojectcustomer10.Model.Appointment;
import com.dilanka456.myprojectcustomer10.Model.Customer;
import com.dilanka456.myprojectcustomer10.Model.Discount;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.dilanka456.myprojectcustomer10.Model.Schedule;
import com.dilanka456.myprojectcustomer10.fcmHelper.FCMClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class One_packageBook_view extends AppCompatActivity {

    private static final String TAG = "onePackBookingTag";
    private static final int PAYHERE_REQUEST = 220;
    ImageView salon_img, pack_img;
    TextView salon_name_lbl, salon_slogan_lbl, pack_name_lbl, pack_description_lbl, pack_duration_lbl, pack_price_lbl,salon_mobile_fld,book_get_direction;
    Spinner date_spinner_fld, time_spinner_fld;
    Button get_appoint_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String packDocId;
    private String salon_doc_id;
    private String today;
    private String strTime;
    private String endingTime;
    private String customerEmail;
    private String customerDocId;
    private String appointmentDocId;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private String pack_price;
    private int pack_total_amount;
    private AlertDialog.Builder emtdialogBuilder;
    private AlertDialog emtalertDialog;
    private FCMClient fcmClient;
    private String fcm_token;
    private String customerName;
    private String packageName;
    private String appointDate;
    private String customerEmailnew;
    private String customerMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_package_book_view);


        fcmClient = new FCMClient();

//        ------------------------get customer Email--------------------------------
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            customerEmail = acct.getEmail();
        }
//        ---------------------------------get Existing Data-------------------------------------
        Bundle extras = getIntent().getExtras();
        packDocId = extras.getString("packDocId");

//        -----------------------------Assign Feilds----------------------------------------
        salon_img = findViewById(R.id.book_salon_img);
        pack_img = findViewById(R.id.book_pack_img);
        salon_name_lbl = findViewById(R.id.book_salon_name);
        pack_name_lbl = findViewById(R.id.book_pack_name);
        pack_description_lbl = findViewById(R.id.book_pack_description);
        pack_duration_lbl = findViewById(R.id.book_pack_duration);
        pack_price_lbl = findViewById(R.id.book_pack_price);
        salon_mobile_fld = findViewById(R.id.book_salon_mobile);
        book_get_direction= findViewById(R.id.book_get_direction);


        date_spinner_fld = findViewById(R.id.booking_date_spinner);

        time_spinner_fld = findViewById(R.id.booking_time_spinner);
        salon_slogan_lbl = findViewById(R.id.book_salon_slogan);
        get_appoint_btn = findViewById(R.id.btn_appooint);


//        -------------------------------------------get Customer-------------------------------
        db.collection("Customer").whereEqualTo("email", customerEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    customerDocId = documentSnapshot.getId();
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    customerMobile = customer.getMobile();
                    customerEmailnew = customer.getEmail();
                    customerName = customer.getName();
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        book_get_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(One_packageBook_view.this,Salon_direction.class);
                intent.putExtra("SalonDocId",salon_doc_id);
                startActivity(intent);
            }
        });

//        ----------------------------get Package-------------------------
        db.collection("Package").document(packDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Package aPackage = documentSnapshot.toObject(Package.class);
                    packageName = aPackage.getName();

                    pack_price = aPackage.getPrice();
                    Picasso.get().load(aPackage.getImg_url()).into(pack_img);
                    pack_name_lbl.setText(aPackage.getName());
                    pack_description_lbl.setText(aPackage.getDescription());
                    pack_duration_lbl.setText(aPackage.getDuration());
                    pack_price_lbl.setText("Rs." + aPackage.getPrice());
                    salon_doc_id = aPackage.getSalon_doc_id();

                    setSalonFields();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "booking_data_error: " + e.getMessage());

            }
        });


        strTime = "08:30";
        endingTime = "17:30";

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        today = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());

        db.collection("Schedule").whereEqualTo("salonDocId", salon_doc_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    Schedule schedule = documentSnapshot.toObject(Schedule.class);
                    if (today.equalsIgnoreCase("Sun")) {
                        strTime = schedule.getSat_open();
                        endingTime = schedule.getSun_close();
                    } else if (today.equalsIgnoreCase("Mon")) {
                        strTime = schedule.getMon_open();
                        endingTime = schedule.getMon_close();
                    } else if (today.equalsIgnoreCase("Tue")) {
                        strTime = schedule.getTue_open();
                        endingTime = schedule.getTue_close();
                    } else if (today.equalsIgnoreCase("Wed")) {
                        strTime = schedule.getWed_open();
                        endingTime = schedule.getWed_close();
                    } else if (today.equalsIgnoreCase("Thu")) {
                        strTime = schedule.getThu_open();
                        endingTime = schedule.getThu_close();
                    } else if (today.equalsIgnoreCase("Fri")) {
                        strTime = schedule.getFri_open();
                        endingTime = schedule.getFri_close();
                    } else if (today.equalsIgnoreCase("Sat")) {
                        strTime = schedule.getSat_open();
                        endingTime = schedule.getSat_close();
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        ArrayList<String> times = getTimes(strTime, endingTime);
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, times);
        time_spinner_fld.setAdapter(timesAdapter);

        ArrayList<String> dates = getDates();
        ArrayAdapter<String> datesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dates);
        date_spinner_fld.setAdapter(datesAdapter);

        get_appoint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAppointmentPop();
            }
        });

    }

    private void setSalonFields() {
        db.collection("Salon").document(salon_doc_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Salon salon = documentSnapshot.toObject(Salon.class);
                    fcm_token = salon.getFcm_token();

                    Picasso.get().load(salon.getLogo_path()).into(salon_img);
                    salon_name_lbl.setText(salon.getName());
                    salon_slogan_lbl.setText(salon.getSlogan());
                    salon_mobile_fld.setText(salon.getContact());

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "booking_data_error: " + e.getMessage());
            }
        });
    }


    public ArrayList<String> getDates() {

        ArrayList<String> dates = new ArrayList<>();
        dates.add("Mar 21");
        dates.add("Mar 22");
        dates.add("Mar 23");

        return dates;

    }

    public ArrayList<String> getTimes(String startTime, String endTime) {

        String[] startTimes = startTime.split(":");
        String startHour = startTimes[0];
        int stHour = Integer.parseInt(startHour);
        String startMinit = startTimes[1];
        int stMinit = Integer.parseInt(startMinit);


        String[] endTimes = endTime.split(":");
        String endHour = endTimes[0];
        int endHournb = Integer.parseInt(endHour);

        int hoursHave = endHournb - stHour;
        ArrayList<String> times = new ArrayList<>();

        for (int i = 1; i <= hoursHave; i++) {

            int i1 = stHour++;
            String timehsr = i1 + ":" + stMinit;

            times.add(String.valueOf(timehsr));
        }


        return times;
    }

    public void placeAppointmentPop() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View myListVIew = getLayoutInflater().inflate(R.layout.booking_card_enater_popup, null);

        Button appointmentSaveBtn = myListVIew.findViewById(R.id.appointment_send_btn);
        ImageView close_btn = myListVIew.findViewById(R.id.close_btn_card_pop);
        TextView total_lbl = myListVIew.findViewById(R.id.pack_total_lbl);
        dialogBuilder.setView(myListVIew);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        int packNbPrice = Integer.parseInt(pack_price);

        pack_total_amount = packNbPrice;

//        ---------------------------------Check this pack has discount------------------------------
        db.collection("Discount").whereEqualTo("package_doc_id", packDocId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    Discount discount = documentSnapshot.toObject(Discount.class);
                    int discountNb = Integer.parseInt(discount.getDiscount());
                    int minDis = packNbPrice / 100 * discountNb;
                    int TotalAmount = packNbPrice - minDis;
                    pack_total_amount = TotalAmount;
                    total_lbl.setText("Rs." + TotalAmount);

                } else {
                    total_lbl.setText("Rs." + packNbPrice);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        appointmentSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Appointment").whereEqualTo("customerDocId", customerDocId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            EmptyPopOpen();
                        } else {
//                            payNow();

                            SaveAppointMent();
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




    private void SaveAppointMent() {
        Appointment appointment = new Appointment();
        appointment.setCustomerDocId(customerDocId);
        appointment.setSalonDocId(salon_doc_id);
        appointment.setPackageDocId(packDocId);
        appointment.setDate(date_spinner_fld.getSelectedItem().toString());
        appointment.setTime(time_spinner_fld.getSelectedItem().toString());
        appointment.setTotalPrice(String.valueOf(pack_total_amount));
        appointment.setStatus("Placed");

        appointDate = date_spinner_fld.getSelectedItem().toString();

        db.collection("Appointment").add(appointment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

//                ----------------------------------Add FCM-----------------------

                fcmClient.execute(fcm_token, "Appointment Received", "Name :" + customerName + " Package: " + packageName + "Date: " + appointDate);

                appointmentDocId = documentReference.getId();
                Intent intent = new Intent(One_packageBook_view.this, Home.class);
                intent.putExtra("email", customerEmail);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void EmptyPopOpen() {
        emtdialogBuilder = new AlertDialog.Builder(this);
        final View appView = getLayoutInflater().inflate(R.layout.home_appointment_not_pop, null);
        ImageView closeView = appView.findViewById(R.id.empty_pop_home_close);
        TextView headerView = appView.findViewById(R.id.empty_pop_hed_lbl);
        headerView.setText("Already have an Appointment, Please remove it First...! Or Please wait until it Finished");

        emtdialogBuilder.setView(appView);
        emtalertDialog = emtdialogBuilder.create();
        emtalertDialog.show();

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emtalertDialog.cancel();
            }
        });
    }



}