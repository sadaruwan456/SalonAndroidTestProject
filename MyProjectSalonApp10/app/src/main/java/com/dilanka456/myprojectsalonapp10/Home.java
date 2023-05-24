package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dilanka456.myprojectsalonapp10.Model.Salon;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //alertassign
    private AlertDialog.Builder dialogBuilder,stsDailogBuilder;
    private AlertDialog alertDialog,stsalertDialog;


    private static final String TAG = "HomeTag";
    private String owner_docId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private String email;
    private String uid;

    View create_salon_view,edit_salon_btn,appointment_home_btn,my_salon_btn,home_schedule_btn,home_package_btn;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;

    TextView popupTextView01,salon_name_home;
    Switch online_switch;
    private String salon_doc_id;
    private boolean salonFount;

    ImageView home_logo_view;
    LinearLayout home_main_bar;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        salon_name_home = findViewById(R.id.salon_name_fld_home);
        online_switch = findViewById(R.id.salon_online_btn_home);
        home_main_bar = findViewById(R.id.home_main_bar);





        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        email = extras.getString("email");
        uid = extras.getString("uid");
        owner_docId = extras.getString("owner_docId");



        create_salon_view = findViewById(R.id.create_salon_view);
        drawerLayout = findViewById(R.id.drawer_laout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        ---------------------------Search Existing salon--------------------------------------------
        db.collection("Salon").whereEqualTo("owner_docId",owner_docId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size()>0){

                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                     salon_doc_id = documentSnapshot.getId();
                    Salon salon = documentSnapshot.toObject(Salon.class);
                    salon_name_home.setText(salon.getName());


                    online_switch.setChecked(salon.isStatus());


                    ImageView cr_salon_img=findViewById(R.id.imageView7);
                    TextView cr_txt = findViewById(R.id.create_salon_txt);

                    cr_salon_img.setImageResource(R.drawable.ic_mybusiness);
                    cr_txt.setText("My Salon");

                  salonFount = true;
                  home_main_bar.setVisibility(View.VISIBLE);



//                    create_salon_view.setVisibility(View.INVISIBLE);
//                    cr_salon_img.setVisibility(View.INVISIBLE);
//                    cr_txt.setVisibility(View.INVISIBLE);
                }else {

                    salonFount = false;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Salon_search_error: "+e.getMessage());
            }
        });



            online_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (salonFount){
                    salonStatusPopup(isChecked);

                    }

                }
            });




//        ----------------------------Package btn Action--------------------------------
        home_package_btn = findViewById(R.id.home_package_btn);
        home_package_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salonFount){
                Intent intent = new Intent(Home.this,Package_list.class);
                intent.putExtra("salonDocId",salon_doc_id);
                startActivity(intent);

                }else{
                    MysalonListPopup();
                }
            }
        });



//        -------------------------------schedule btn action------------------------------
        home_schedule_btn = findViewById(R.id.home_schedule_btn);
        home_schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salonFount){
                Intent intent = new Intent(Home.this,Schedule_create_Edit.class);
                intent.putExtra("salon_doc_id",salon_doc_id);
                intent.putExtra("salon_found",salonFount);
                startActivity(intent);

                }else{
                    MysalonListPopup();
                }
            }
        });




//        -----------------------------Edite Salon Btn Action---------------------------------
        edit_salon_btn = findViewById(R.id.edit_salon_btn);

        edit_salon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (salonFount){

                Intent intent = new Intent(Home.this,Edite_salon.class);
                intent.putExtra("owner_docId",owner_docId);
                intent.putExtra("uid",uid);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                startActivity(intent);
                }else{

                    MysalonListPopup();
                }
            }
        });



//        -----------------------------------Appointment Btn Action-------------------------------------

        appointment_home_btn = findViewById(R.id.appointment_home_btn);
        appointment_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salonFount){
                Intent intent = new Intent(Home.this,Apointments.class);
                intent.putExtra("SalonDocId",salon_doc_id);
                startActivity(intent);

                }else{
                    MysalonListPopup();
                }
            }
        });


         menu = navigationView.getMenu();

        if (salonFount){
            menu.findItem(R.id.nav_create_salon).setVisible(false);

        }else{
        menu.findItem(R.id.nav_mysalon).setVisible(false);
        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navi,R.string.close_navi);
        drawerLayout.addDrawerListener(toggle);
        navigationView.setCheckedItem(R.id.nav_home);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        create_salon_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (salonFount){
                    Intent intent = new Intent(Home.this, MySalon.class);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("uid",uid);
                intent.putExtra("owner_docId",owner_docId);
                intent.putExtra("salon_doc_id",salon_doc_id);
                startActivity(intent);

                }else{

                Intent intent = new Intent(Home.this,Create_salon.class);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("uid",uid);
                intent.putExtra("owner_docId",owner_docId);
                startActivity(intent);

                }
            }
        });




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_mysalon:
                Intent intent = new Intent(Home.this, MySalon.class);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("uid",uid);
                intent.putExtra("owner_docId",owner_docId);
                intent.putExtra("salon_doc_id",salon_doc_id);
                startActivity(intent);
                break;

            case R.id.nav_create_salon:
                Intent intent2 = new Intent(Home.this,Create_salon.class);
                intent2.putExtra("name",name);
                intent2.putExtra("email",email);
                intent2.putExtra("uid",uid);
                intent2.putExtra("owner_docId",owner_docId);
                startActivity(intent2);
                break;
            case R.id.nav_appointment:
                //methanata, me usergen salon ekak thiyenawada kiyala if ekakin check karanna oni
                if (salonFount){
                    Intent intent3 = new Intent(Home.this,Apointments.class);
                    intent3.putExtra("SalonDocId",salon_doc_id);
                    startActivity(intent3);

                }else{
                    MysalonListPopup();
                }
                break;
            case R.id.nav_schedule:

                //methanata, me usergen salon ekak thiyenawada kiyala if ekakin check karanna oni
                if (salonFount){
                    Intent intent4 = new Intent(Home.this,Schedule_create_Edit.class);
                    intent4.putExtra("salon_doc_id",salon_doc_id);
                    intent4.putExtra("salon_found",salonFount);
                    startActivity(intent4);

                }else{
                    MysalonListPopup();
                }
                break;
            case R.id.nav_myaccount:
                Intent intent5 = new Intent(Home.this,Register.class);
                intent5.putExtra("MyaccountFound",true);
                intent5.putExtra("OwnerDocId",owner_docId);
                startActivity(intent5);
                break;
            case R.id.nav_logout:
                signOut();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();
        }
    }


    public void MysalonListPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View myListVIew = getLayoutInflater().inflate(R.layout.my_salon_list_popup,null);

        ImageView close_btn = myListVIew.findViewById(R.id.close_btn_salon_list_pop);


        dialogBuilder.setView(myListVIew);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    public void salonStatusPopup(boolean isCheck){
        stsDailogBuilder = new AlertDialog.Builder(this);
        final View myListVIew = getLayoutInflater().inflate(R.layout.salon_online_pop,null);
        ImageView popCLose = myListVIew.findViewById(R.id.salon_online_pop_close);
        Button okBtnPop = myListVIew.findViewById(R.id.salon_online_pop_ok_btn);

        stsDailogBuilder.setView(myListVIew);
        stsalertDialog = stsDailogBuilder.create();
        stsalertDialog.show();

        popCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stsalertDialog.cancel();
            }
        });

        okBtnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Salon").document(salon_doc_id).update("status",isCheck).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Home.this, "Salon Status Updated", Toast.LENGTH_SHORT).show();
                        stsalertDialog.cancel();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


    }


    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                       Intent intent = new Intent(Home.this,Login.class);
                       startActivity(intent);
                       finish();
                    }
                });
        // [END auth_fui_signout]
    }

}