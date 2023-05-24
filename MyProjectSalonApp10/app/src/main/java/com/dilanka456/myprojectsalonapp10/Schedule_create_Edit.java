package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;


import com.dilanka456.myprojectsalonapp10.Model.Schedule;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Schedule_create_Edit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText sun_open_hours, sun_open_minites,sun_close_hours,sun_close_minites,mon_open_hours,mon_open_minites,mon_close_hours,mon_close_minites,tue_open_hours,tue_open_minites,tue_close_hours,tue_close_minites;
    EditText wed_open_hours,wed_open_minites,wed_close_hours,wed_close_minites,thu_open_hours,thu_open_minites,thu_close_hours,thu_close_minites,fri_open_hours,fri_open_minites,fri_close_hours,fri_close_minites,sat_open_hours,sat_open_minites,sat_close_hours,sat_close_minites;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Switch sun_swt,mon_swt,tue_swt,wed_swt,thu_swt,fri_swt,sat_swt;
    EditText sun_rsn,mon_rsn,tue_rsn,wed_rsn,thu_rsn,fri_rsn,sat_rsn;


    Button save_btn,remove_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean sun_swtChecked,mon_swtChecked,tue_swtChecked,wed_swtChecked,thu_swtChecked,fri_swtChecked,sat_swtChecked;
    private String schedule_doc_id;
    private boolean scheduleFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_create__edit);

        save_btn = findViewById(R.id.schedule_save_btn);
        remove_btn = findViewById(R.id.schedule_remove_btn);


//        -----------------------------------get existing data-------------------------------------
        Bundle bundle = getIntent().getExtras();
        String salon_doc_id = bundle.getString("salon_doc_id");
        boolean salon_found = (boolean) bundle.get("salon_found");





//        --------------------------Switch btn assign--------------------------------------------
//        -
        sun_swt = findViewById(R.id.sun_close_swt);
        mon_swt = findViewById(R.id.mon_close_swt);
        tue_swt = findViewById(R.id.tue_close_swt);
        wed_swt = findViewById(R.id.wed_close_swt);
        thu_swt = findViewById(R.id.thu_close_swt);
        fri_swt = findViewById(R.id.fri_close_swt);
        sat_swt = findViewById(R.id.sat_close_swt);

//        -----------------------switch btn sts-------------------------------
         sun_swtChecked = sun_swt.isChecked();
         mon_swtChecked = mon_swt.isChecked();
         tue_swtChecked = tue_swt.isChecked();
         wed_swtChecked = wed_swt.isChecked();
         thu_swtChecked = thu_swt.isChecked();
         fri_swtChecked = fri_swt.isChecked();
         sat_swtChecked = sat_swt.isChecked();


//         ---------------------------------------Reason fld Assing----------------------------
        sun_rsn = findViewById(R.id.sun_reason_fld);
        mon_rsn = findViewById(R.id.mon_reason_fld);
        tue_rsn = findViewById(R.id.tue_reason_fld);
        wed_rsn = findViewById(R.id.wed_reason_fld);
        thu_rsn = findViewById(R.id.thu_reason_fld);
        fri_rsn = findViewById(R.id.fri_reason_fld);
        sat_rsn = findViewById(R.id.sat_reason_fld);



//        ----------------------time adapters ----------------------------------------
//        String[] hours ={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
//        String[] minites = {"00","05","10","15","20","25","30","35","40","45","50","55","60"};
//
//        ArrayAdapter hoursAdapter =new ArrayAdapter(this,android.R.layout.simple_spinner_item,hours);
//        ArrayAdapter minitesAdapter =new ArrayAdapter(this,android.R.layout.simple_spinner_item,minites);


//        ---------------------------------Spinners declaretion--------------------------------------------
//        ---sun----
        sun_open_hours = findViewById(R.id.sun_open_hours_fld);
        sun_open_minites = findViewById(R.id.sun_open_minites_fld);
        sun_close_hours = findViewById(R.id.sun_close_hours_fld);
        sun_close_minites = findViewById(R.id.sun_close_minites_fld);



//        ----mon-----
        mon_open_hours = findViewById(R.id.mon_open_hours_fld);
        mon_open_minites = findViewById(R.id.mon_open_minites_fld);
        mon_close_hours = findViewById(R.id.mon_close_hours_fld);
        mon_close_minites = findViewById(R.id.mon_close_minites_fld);


//        ----TUE-------
        tue_open_hours = findViewById(R.id.tue_open_hours_fld);
        tue_open_minites = findViewById(R.id.tue_open_minites_fld);
        tue_close_hours = findViewById(R.id.tue_close_hours_fld);
        tue_close_minites = findViewById(R.id.tue_close_minites_fld);


//        -----WEB-------
        wed_open_hours = findViewById(R.id.wed_open_hours_fld);
        wed_open_minites = findViewById(R.id.wed_open_minites_fld);
        wed_close_hours = findViewById(R.id.wed_close_hours_fld);
        wed_close_minites = findViewById(R.id.wed_close_minites_fld);



//        -----THU-------
        thu_open_hours = findViewById(R.id.thu_open_hours_fld);
        thu_open_minites = findViewById(R.id.thu_open_minites_fld);
        thu_close_hours = findViewById(R.id.thu_close_hours_fld);
        thu_close_minites = findViewById(R.id.thu_close_minites_fld);


//        ------FRI----
        fri_open_hours = findViewById(R.id.fri_open_hours_fld);
        fri_open_minites = findViewById(R.id.fri_open_minites_fld);
        fri_close_hours = findViewById(R.id.fri_close_hours_fld);
        fri_close_minites = findViewById(R.id.fri_close_minites_fld);

//        -----SAT-----
        sat_open_hours = findViewById(R.id.sat_open_hours_fld);
        sat_open_minites = findViewById(R.id.sat_open_minites_fld);
        sat_close_hours = findViewById(R.id.sat_close_hours_fld);
        sat_close_minites = findViewById(R.id.sat_close_minites_fld);




//        ---------------------------------------------------Drawer menu Set-----------------------------
        drawerLayout = findViewById(R.id.drawer_layout_schedule);
        navigationView = findViewById(R.id.nav_view_schedule);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navi,R.string.close_navi);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




//        -----------------------------------Search salon schedule-------------------------------
        if (salon_found){
            db.collection("Schedule").whereEqualTo("salonDocId",salon_doc_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.getDocuments().size()>0){
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        schedule_doc_id = documentSnapshot.getId();

                        Schedule scheduleTxt = documentSnapshot.toObject(Schedule.class);

//                        ------------------------search data set to field--------------------------------
                        String sun_open = scheduleTxt.getSun_open();
                        String[] sun_open_spt = sun_open.split(":");
                        sun_open_hours.setText(sun_open_spt[0]);
                        sun_open_minites.setText(sun_open_spt[1]);

                        String sun_close = scheduleTxt.getSun_close();
                        String[] sun_close_spt = sun_close.split(":");
                        sun_close_hours.setText(sun_close_spt[0]);
                        sun_close_minites.setText(sun_close_spt[1]);

                        sun_swt.setChecked(scheduleTxt.isSun_sts());
                        sun_rsn.setText(scheduleTxt.getSun_reason());

//                        -----------------------------------------------------------------
                        String mon_open = scheduleTxt.getMon_open();
                        String[] mon_open_spt = mon_open.split(":");
                        mon_open_hours.setText(mon_open_spt[0]);
                        mon_open_minites.setText(mon_open_spt[1]);

                        String mon_close = scheduleTxt.getMon_close();
                        String[] mon_close_spt = mon_close.split(":");
                        mon_close_hours.setText(mon_close_spt[0]);
                        mon_close_minites.setText(mon_close_spt[1]);

                        mon_swt.setChecked(scheduleTxt.isMon_sts());
                        mon_rsn.setText(scheduleTxt.getMon_reason());

//                        -----------------------------------------------------------------------
                        String tue_open = scheduleTxt.getTue_open();
                        String[] tue_open_spt = tue_open.split(":");
                        tue_open_hours.setText(tue_open_spt[0]);
                        tue_open_minites.setText(tue_open_spt[1]);

                        String tue_close = scheduleTxt.getTue_close();
                        String[] tue_close_spt = tue_close.split(":");
                        tue_close_hours.setText(tue_close_spt[0]);
                        tue_close_minites.setText(tue_close_spt[1]);

                        tue_swt.setChecked(scheduleTxt.isTue_sts());
                        tue_rsn.setText(scheduleTxt.getTue_reason());

//                        ---------------------------------------------------------------
                        String wed_open = scheduleTxt.getWed_open();
                        String[] wed_open_spt = wed_open.split(":");
                        wed_open_hours.setText(wed_open_spt[0]);
                        wed_open_minites.setText(wed_open_spt[1]);

                        String wed_close = scheduleTxt.getWed_close();
                        String[] wed_close_spt = wed_close.split(":");
                        wed_close_hours.setText(wed_close_spt[0]);
                        wed_close_minites.setText(wed_close_spt[1]);

                        wed_swt.setChecked(scheduleTxt.isWed_sts());
                        wed_rsn.setText(scheduleTxt.getWed_reason());

//                        ----------------------------------------------------------------------

                        String thu_open = scheduleTxt.getThu_open();
                        String[] thu_open_spt = thu_open.split(":");
                        thu_open_hours.setText(thu_open_spt[0]);
                        thu_open_minites.setText(thu_open_spt[1]);

                        String thu_close = scheduleTxt.getThu_close();
                        String[] thu_close_spt = thu_close.split(":");
                        thu_close_hours.setText(thu_close_spt[0]);
                        thu_close_minites.setText(thu_close_spt[1]);

                        thu_swt.setChecked(scheduleTxt.isThu_sts());
                        thu_rsn.setText(scheduleTxt.getThu_reason());

//                        -------------------------------------------------------------------------

                        String fri_open = scheduleTxt.getFri_open();
                        String[] fri_open_spt = fri_open.split(":");
                        fri_open_hours.setText(fri_open_spt[0]);
                        fri_open_minites.setText(fri_open_spt[1]);

                        String fri_close = scheduleTxt.getFri_close();
                        String[] fri_close_spt = fri_close.split(":");
                        fri_close_hours.setText(fri_close_spt[0]);
                        fri_close_minites.setText(fri_close_spt[1]);

                        fri_swt.setChecked(scheduleTxt.isFri_sts());
                        fri_rsn.setText(scheduleTxt.getFri_reason());

//                        ---------------------------------------------------------------------------
                        String sat_open = scheduleTxt.getSat_open();
                        String[] sat_open_spt = sat_open.split(":");
                        sat_open_hours.setText(sat_open_spt[0]);
                        sat_open_minites.setText(sat_open_spt[1]);

                        String sat_close = scheduleTxt.getSat_close();
                        String[] sat_close_spt = sat_close.split(":");
                        sat_close_hours.setText(sat_close_spt[0]);
                        sat_close_minites.setText(sat_close_spt[1]);

                        sat_swt.setChecked(scheduleTxt.isSat_sts());
                        sat_rsn.setText(scheduleTxt.getSat_reason());

                        scheduleFound = true;
                        remove_btn.setVisibility(View.VISIBLE);


                    }else{

                        scheduleFound = false;

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

//        ------------------------------------------Save btn action----------------------------------------------
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule schedule = new Schedule();

                schedule.setSalonDocId(salon_doc_id);
                schedule.setSun_open(sun_open_hours.getText().toString()+":"+sun_open_minites.getText().toString());
                schedule.setSun_close(sun_close_hours.getText().toString()+":"+sun_close_minites.getText().toString());
                schedule.setSun_reason(sun_rsn.getText().toString());
                schedule.setSun_sts(sun_swt.isChecked());

                schedule.setMon_open(mon_open_hours.getText().toString()+":"+mon_open_minites.getText().toString());
                schedule.setMon_close(mon_close_hours.getText().toString()+":"+mon_close_minites.getText().toString());
                schedule.setMon_reason(mon_rsn.getText().toString());
                schedule.setMon_sts(mon_swt.isChecked());

                schedule.setTue_open(tue_open_hours.getText().toString()+":"+tue_open_minites.getText().toString());
                schedule.setTue_close(tue_close_hours.getText().toString()+":"+tue_close_minites.getText().toString());
                schedule.setTue_reason(tue_rsn.getText().toString());
                schedule.setTue_sts(tue_swt.isChecked());

                schedule.setWed_open(wed_open_hours.getText().toString()+":"+wed_open_minites.getText().toString());
                schedule.setWed_close(wed_close_hours.getText().toString()+":"+wed_close_minites.getText().toString());
                schedule.setWed_reason(wed_rsn.getText().toString());
                schedule.setWed_sts(wed_swt.isChecked());

                schedule.setThu_open(thu_open_hours.getText().toString()+":"+thu_open_minites.getText().toString());
                schedule.setThu_close(thu_close_hours.getText().toString()+":"+thu_close_minites.getText().toString());
                schedule.setThu_reason(thu_rsn.getText().toString());
                schedule.setThu_sts(thu_swt.isChecked());

                schedule.setFri_open(fri_open_hours.getText().toString()+":"+fri_open_minites.getText().toString());
                schedule.setFri_close(fri_close_hours.getText().toString()+":"+fri_close_minites.getText().toString());
                schedule.setFri_reason(fri_rsn.getText().toString());
                schedule.setFri_sts(fri_swt.isChecked());

                schedule.setSat_open(sat_open_hours.getText().toString()+":"+sat_open_minites.getText().toString());
                schedule.setSat_close(sat_close_hours.getText().toString()+":"+sat_close_minites.getText().toString());
                schedule.setSat_reason(sat_rsn.getText().toString());
                schedule.setSat_sts(sat_swt.isChecked());

                if (scheduleFound){
                    Map<String,Object> map = new HashMap<>();
                    map.put("sun_open",schedule.getSun_open());
                    map.put("sun_close",schedule.getSun_close());
                    map.put("sun_reason",schedule.getSun_reason());
                    map.put("sun_sts",schedule.isSun_sts());

                    map.put("mon_open",schedule.getMon_open());
                    map.put("mon_close",schedule.getMon_close());
                    map.put("mon_reason",schedule.getMon_reason());
                    map.put("mon_sts",schedule.isMon_sts());

                    map.put("tue_open",schedule.getThu_open());
                    map.put("tue_close",schedule.getThu_close());
                    map.put("tue_reason",schedule.getTue_reason());
                    map.put("tue_sts", schedule.isTue_sts());

                    map.put("wed_open",schedule.getWed_open());
                    map.put("wed_close",schedule.getWed_close());
                    map.put("wed_reason",schedule.getWed_reason());
                    map.put("wed_sts",schedule.isWed_sts());

                    map.put("thu_open",schedule.getThu_open());
                    map.put("thu_close",schedule.getThu_close());
                    map.put("thu_reason",schedule.getThu_reason());
                    map.put("thu_sts",schedule.isThu_sts());

                    map.put("fri_open",schedule.getFri_open());
                    map.put("fri_close",schedule.getFri_close());
                    map.put("fri_reason",schedule.getFri_reason());
                    map.put("fri_sts",schedule.isFri_sts());

                    map.put("sat_open",schedule.getSat_open());
                    map.put("sat_close",schedule.getSat_close());
                    map.put("sat_reason",schedule.getSat_reason());
                    map.put("sat_sts",schedule.isSat_sts());


                    db.collection("Schedule").document(schedule_doc_id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onBackPressed();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                        }
                    });

                }else{

                db.collection("Schedule").add(schedule).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Schedule_create_Edit.this, "Schedule_Saved", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Schedule_create_Edit.this, "Schedule_save_error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                }



            }
        });


        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scheduleFound){
                    db.collection("Schedule").document(schedule_doc_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Schedule_create_Edit.this, "Schedule Deleted", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
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
                break;
            case R.id.nav_appointment:
                break;
            case R.id.nav_schedule:
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
                        Intent intent = new Intent(Schedule_create_Edit.this,Login.class);
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
}