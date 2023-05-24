package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dilanka456.myprojectsalonapp10.Model.Appointment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class Apointments extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    Button request_btn,accepted_btn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private String salonDocId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apointments);

//        ----------------------------------get Exist data-------------------------------
        Bundle extras = getIntent().getExtras();
        salonDocId = extras.getString("SalonDocId");
//        --------------------------------Drawer Menu ----------------------------------------
        drawerLayout = findViewById(R.id.drawer_laout_01);
        navigationView = findViewById(R.id.nav_view_01);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_create_salon).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navi,R.string.close_navi);
        drawerLayout.addDrawerListener(toggle);
        navigationView.setCheckedItem(R.id.nav_appointment);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

//        -------------------------------------Defult Fragment load----------------------------------------

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Appointment_request_fragment appointment_request_fragment = new Appointment_request_fragment();
        transaction.add(R.id.appointment_container,appointment_request_fragment,"request_fragment");
        transaction.commit();



//        --------------------------------Fragment btns--------------------------
        request_btn = findViewById(R.id.appointment_reuest_btn);
        request_btn.setBackgroundResource(R.drawable.appointment_btn_back_style);
        accepted_btn = findViewById(R.id.appointment_accept_btn);

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();

                Appointment_request_fragment appointment_request_fragment = new Appointment_request_fragment();
                transaction.replace(R.id.appointment_container,appointment_request_fragment,"request_fragment");
                transaction.commit();

                request_btn.setBackgroundResource(R.drawable.appointment_btn_back_style);
                accepted_btn.setBackgroundResource(R.drawable.appointment_btn_style_02);
            }
        });

        accepted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                Appointment_accepted_fragment appointment_accepted_fragment = new Appointment_accepted_fragment();
                transaction.replace(R.id.appointment_container,appointment_accepted_fragment,"accepted_fragment");
                transaction.commit();


                accepted_btn.setBackgroundResource(R.drawable.appointment_btn_back_style);
                request_btn.setBackgroundResource(R.drawable.appointment_btn_style_02);

            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                onBackPressed();
                break;
            case R.id.nav_mysalon:
                //methanata, me usergen salon ekak thiyenawada kiyala if ekakin check karanna oni
                Intent intent2 = new Intent(Apointments.this,MySalon.class);
                startActivity(intent2);
                break;
            case R.id.nav_appointment:
                break;
            case R.id.nav_logout:
                signOut();
                break;
        }
        return true;
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(Apointments.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
        // [END auth_fui_signout]
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();
        }
    }

    public String getSalonDocId(){
        String mySalonDocId = salonDocId;
        return mySalonDocId;
    }
}