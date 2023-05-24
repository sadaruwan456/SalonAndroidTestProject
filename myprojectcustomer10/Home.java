package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.dilanka456.myprojectcustomer10.Model.Appointment;
import com.dilanka456.myprojectcustomer10.Model.Customer;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PAYHERE_REQUEST = 250;
    private static final String TAG = "HomeTag";
    ImageView all_salon_btn_image, top_rated_btn_image, discount_btn_image, pro_pic, home_logo_view, home_appoint_view;


    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public String email;
    private String name;
    private String pro_url;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String customerDocId;

    TextView appointment_count_lbl;
    private boolean appointHave;
    private AlertDialog.Builder dialogBuilder, emtdialogBuilder;
    private AlertDialog alertDialog, emtalertDialog;
    private Appointment appointmentObj;
    private String appointmentDocId;
    private String packageName;
    private String customerMobile;
    private String customerName;
    private String customerEmail;
    private String customerPro_pic_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        -------------------------------get Existing Data----------------------------------------------------
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");

        if (extras.getString("name") != null) {
            name = extras.getString("name");

        } else if (extras.getString("pro_url") != null) {

            pro_url = extras.getString("pro_url");
        }


        pro_pic = findViewById(R.id.home_pro_pic);

        Picasso.get().load(pro_url).into(pro_pic);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Home_default_fragment home_default_fragment = new Home_default_fragment();
        transaction.replace(R.id.home_fragment_container, home_default_fragment, "default_fragment");
        transaction.commit();


//       -----------------------------home navigate logo press--------------------------------
//        home_logo_view = findViewById(R.id.home_logo_view);
//        home_logo_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//
//            }
//        });


//        ----------------------------------get and set appointment number and Status---------------------------
        appointment_count_lbl = findViewById(R.id.appoint_number_lbl);

        db.collection("Customer").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    Customer customer = documentSnapshot.toObject(Customer.class);

                    customerName = customer.getName();
                    customerEmail = customer.getEmail();
                    customerMobile = customer.getMobile();
                    customerDocId = documentSnapshot.getId();
                    customerPro_pic_url = customer.getPro_pic_Url();

                    getAppointment(customerDocId);

                } else {


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        home_appoint_view = findViewById(R.id.home_appoint_show_view);
        home_appoint_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appointHave) {

                    HomePopAppointment();
                } else {

                    EmptyPopOpen();


                }
            }
        });


//        ----------------------------------Discount load btn action-----------------------------
        discount_btn_image = findViewById(R.id.discount_btn_image);
        discount_btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();

                Discount_list_fragment discount_list_fragment = new Discount_list_fragment();
                transaction.replace(R.id.home_fragment_container, discount_list_fragment, "discount_fragment");
                transaction.commit();

                top_rated_btn_image.setBackgroundResource(R.drawable.navi_ic_btn_style);
                all_salon_btn_image.setBackgroundResource(R.drawable.navi_ic_btn_style);
                discount_btn_image.setBackgroundResource(R.drawable.home_navi_btn_action_style);
            }
        });


//       ---------------------------Top rated load btn action-----------------------------
        top_rated_btn_image = findViewById(R.id.top_rated_btn_image);
        top_rated_btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();

                Top_rated_List_Fragment top_rated_list_fragment = new Top_rated_List_Fragment();
                transaction.replace(R.id.home_fragment_container, top_rated_list_fragment, "top_rated_list");
                transaction.commit();
                top_rated_btn_image.setBackgroundResource(R.drawable.home_navi_btn_action_style);
                all_salon_btn_image.setBackgroundResource(R.drawable.navi_ic_btn_style);
                discount_btn_image.setBackgroundResource(R.drawable.navi_ic_btn_style);

            }
        });

//        -----------------------all salon load btn action----------------------------
        all_salon_btn_image = findViewById(R.id.all_salon_btn_image);
        all_salon_btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                All_salon_fragment all_salon_fragment = new All_salon_fragment();
                transaction.replace(R.id.home_fragment_container, all_salon_fragment, "all_salon");
                transaction.commit();
                all_salon_btn_image.setBackgroundResource(R.drawable.home_navi_btn_action_style);
                discount_btn_image.setBackgroundResource(R.drawable.navi_ic_btn_style);
                top_rated_btn_image.setBackgroundResource(R.drawable.navi_ic_btn_style);

            }
        });


        //Define menu items
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        ImageView menu_pro_pic =  headerView.findViewById(R.id.menu_header_pro_img);
        Picasso.get().load(customerPro_pic_url).into(menu_pro_pic);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //menu Actions
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navi, R.string.close_navi);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


    }


    private void getAppointment(String customerDocId) {
        db.collection("Appointment").whereEqualTo("customerDocId", customerDocId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    appointmentObj = documentSnapshot.toObject(Appointment.class);
                    appointmentDocId = documentSnapshot.getId();


                    appointment_count_lbl.setText(String.valueOf(1));

                    appointHave = true;


                } else {
                    appointment_count_lbl.setText(String.valueOf(0));

                    appointHave = false;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this, "Appointment get Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                Intent intent5 = new Intent(Home.this,Edite_profile.class);
                intent5.putExtra("CustomerDocId",customerDocId);
                startActivity(intent5);
                break;
            case R.id.nav_rate_us:
                Intent intent = new Intent(Home.this, Rate_Us.class);
                startActivity(intent);
                break;
            case R.id.nav_about_us:
                Intent intent1 = new Intent(Home.this, AboutUs.class);
                startActivity(intent1);
                break;
            case R.id.nav_contact:
                Intent intent2 = new Intent(Home.this,ContactUs.class);
                startActivity(intent2);
                break;
        }
        return true;
    }

    public String getEmail() {

        String customerEmail = email;

        return customerEmail;

    }

    public void EmptyPopOpen() {
        emtdialogBuilder = new AlertDialog.Builder(this);
        final View appView = getLayoutInflater().inflate(R.layout.home_appointment_not_pop, null);
        ImageView closeView = appView.findViewById(R.id.empty_pop_home_close);

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

    public void HomePopAppointment() {


        dialogBuilder = new AlertDialog.Builder(this);
        final View appView = getLayoutInflater().inflate(R.layout.appointment_view_home_pop, null);

        ImageView close_btn = appView.findViewById(R.id.home_pop_close_view);
        ImageView hPack_img = appView.findViewById(R.id.home_pop_pack_img);
        TextView hPack_name = appView.findViewById(R.id.home_pop_pack_name);
        TextView hPack_price = appView.findViewById(R.id.home_pop_pack_price);
        TextView happoint_date = appView.findViewById(R.id.home_pop_apoint_date);
        TextView happoint_time = appView.findViewById(R.id.home_pop_apoint_time);
        TextView Happoint_status = appView.findViewById(R.id.home_pop_apoint_status);
        Button Hremove_btn = appView.findViewById(R.id.home_pop_apoint_remove_btn);
        Button secondRemove = appView.findViewById(R.id.app_pop_second_remove_btn);
        TextView durationPop = appView.findViewById(R.id.home_pop_pack_duration);


        dialogBuilder.setView(appView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        db.collection("Package").document(appointmentObj.getPackageDocId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Package aPackage = documentSnapshot.toObject(Package.class);

                    packageName = aPackage.getName();
                    Picasso.get().load(aPackage.getImg_url()).into(hPack_img);
                    hPack_name.setText(aPackage.getName());
                    durationPop.setText(aPackage.getDuration()+" Minutes");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        db.collection("Appointment").document(appointmentDocId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    Appointment appointmentnew = value.toObject(Appointment.class);

                    hPack_price.setText("Rs."+appointmentnew.getTotalPrice());
                    happoint_date.setText(appointmentnew.getDate());
                    happoint_time.setText(appointmentnew.getTime());


                    if (appointmentnew.getStatus().equalsIgnoreCase("Placed")){
                        Happoint_status.setText("Your Appointment is not accept yet, Still Reviewing");
                        Happoint_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gradient01));
                        Hremove_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteAppointment();
                            }
                        });

                    }else if (appointmentnew.getStatus().equalsIgnoreCase("Accepted")){
                        Happoint_status.setText("Your Appointment is Accepted...!");
                        Happoint_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gradient01));
                        Hremove_btn.setText("Pay Now");
                        Hremove_btn.setBackgroundResource(R.drawable.btn_style_green);

                        secondRemove.setVisibility(View.VISIBLE);
                        secondRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteAppointment();
                            }
                        });

                        Hremove_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                payNow();

                            }
                        });
                    }else if(appointmentnew.getStatus().equalsIgnoreCase("Payed")){


                        Happoint_status.setText("Your Appointment is Placed...!");
                        Happoint_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gradient01));
                        Hremove_btn.setText("Salon Direction");
                        Hremove_btn.setBackgroundResource(R.drawable.btn_style_green);

                        Hremove_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                payNow();

                                goToDitection();

                            }
                        });

                    }else if(appointmentnew.getStatus().equalsIgnoreCase("Rejected")){

                        Happoint_status.setText("Your Appointment is Rejected, Please remove appointment then you can get another...");
                        Happoint_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                        Hremove_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteAppointment();
                            }
                        });
                    }else{
                        appointment_count_lbl.setText(String.valueOf(0));
                        appointHave=false;

                    }

                }else{
                    appointment_count_lbl.setText(String.valueOf(0));
                    appointHave=false;
                }
            }
        });


    }


    //    ------------------------------------------------------Payment GetWay------------------------------------------
    public void payNow() {

        InitRequest req = new InitRequest();
        req.setMerchantId("1214236");       // Your Merchant PayHere ID
        req.setMerchantSecret("4DtSEK8JDbV4kpFDtg4bLf4EqlA3STdH14ExBryDUG05"); // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
        req.setAmount(Integer.parseInt(appointmentObj.getTotalPrice()));             // Final Amount to be charged
        req.setOrderId(appointmentObj.getPackageDocId());        // Unique Reference ID
        req.setItemsDescription(packageName);  // Item description title
        req.setCustom1("This is the custom message 1");
        req.setCustom2("This is the custom message 2");
        req.getCustomer().setFirstName(customerName);
        req.getCustomer().setLastName(" ");
        req.getCustomer().setEmail(customerEmail);
        req.getCustomer().setPhone(customerMobile);
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");


        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    private void deleteAppointment() {
        db.collection("Appointment").document(appointmentDocId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Home.this, "Your Appointment Deleted", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
                appointment_count_lbl.setText(String.valueOf(0));
                appointHave = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this, "Delete Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });
    }

    private void goToDitection() {

        String salonDocId = appointmentObj.getSalonDocId();
        Intent intent = new Intent(Home.this, Salon_direction.class);
        intent.putExtra("SalonDocId", salonDocId);
        startActivity(intent);
    }

    public void updateAppointmentPay() {
        db.collection("Appointment").document(appointmentDocId).update("status", "Payed").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                alertDialog.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess())

                        updateAppointmentPay();
                msg = "Activity result:" + response.getData().toString();

                msg = "Result:" + response.toString();

                msg = "Result: no response";
                Log.d(TAG, msg);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null) ;


            }
        }
    }


}