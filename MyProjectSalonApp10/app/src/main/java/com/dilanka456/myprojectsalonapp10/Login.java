package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.dilanka456.myprojectsalonapp10.Model.Owner;
import com.dilanka456.myprojectsalonapp10.Model.Salon;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginTag";
    EditText email_fld,password_fld;
    CheckBox showBox;
    Button login_btn, google_btn, popup_try_btn,popup_regi_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int RC_SIGN_IN = 123;
    TextView forgotpw_lbl;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private String name;
    private String email;
    private String uid;
    private String FCVToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initeFCM();

        //        ---------------------------Assign Field--------------------------------------------------------------------
        email_fld = findViewById(R.id.login_email_fld);
        password_fld = findViewById(R.id.login_password_fld);
        showBox = findViewById(R.id.login_show_box);

        login_btn = findViewById(R.id.login_login_btn);
        google_btn = findViewById(R.id.login_google_btn);

        forgotpw_lbl = findViewById(R.id.login_forgot_lbl);

//        ------------------------------forget password lbl Action------------------------------
        forgotpw_lbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });






        //        ------------------------------------------check box design code---------------------------------------------------------------
        showBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    password_fld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password_fld.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
//        ------------------------------------end check box design code-----------------------------------------------------------
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Google eken nathuwa email Password gaha log wena wita local db eken che kirima mehi damiya yuthuya

                db.collection("Owner").whereEqualTo("email",email_fld.getText().toString()).whereEqualTo("password",password_fld.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size()>0){
                            //Account Found

                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String ownerDocid = documentSnapshot.getId();
                            Owner owner = documentSnapshot.toObject(Owner.class);

                            getSalonId(ownerDocid);

                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("name",owner.getName());
                            intent.putExtra("email", owner.getEmail());
                            intent.putExtra("uid",owner.getAuth_id());
                            intent.putExtra("owner_docId",ownerDocid);
                            Log.d(TAG, "LoginOk_User_Found");
                            startActivity(intent);
                            finish();

                        }else{
                            //Account Not fount

                    //call open popup

                    LoginCantPopup();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"LoginError:"+e.getMessage());
                    }
                });






                }


        });

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Google Auth sterted method calling
                createSignInIntent();
            }
        });


//------------------------End On Create----------------------------------
    }

    private void initeFCM() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()){

                    return;
                }

                FCVToken = task.getResult();

//                Toast.makeText(MainActivity.this, FCVToken, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(

                new AuthUI.IdpConfig.GoogleBuilder().build()
               );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 name = user.getDisplayName();
                email = user.getEmail();
                 uid = user.getUid();
                Log.d(TAG, "Login_ok");


                db.collection("Owner").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        if (queryDocumentSnapshots.getDocuments().size()>0){



                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String owner_docId = documentSnapshot.getId();
                            Owner owner = documentSnapshot.toObject(Owner.class);

                            getSalonId(owner_docId);

                            Intent intent = new Intent(Login.this,Home.class);
                            intent.putExtra("name",owner.getName());
                            intent.putExtra("email", owner.getEmail());
                            intent.putExtra("uid",uid);
                            intent.putExtra("owner_docId",owner_docId);
                            Log.d(TAG, "LoginOk_User_Found");
                            startActivity(intent);
                            finish();


                            }else{

                                Intent intent = new Intent(Login.this,Register.class);
                                intent.putExtra("name",name);
                                intent.putExtra("email", email);
                                intent.putExtra("uid",uid);
                                Log.d(TAG, "LoginOk_User_NotFound");

                                startActivity(intent);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Login_Error: "+e.getMessage());

                    }
                });

//                boolean isRegistered = false;
//                if (!isRegistered){
//                    Toast.makeText(this, "Login_ok", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Login.this,Register.class);
//                    intent.putExtra("name",name);
//                    intent.putExtra("email", email);
//                    intent.putExtra("uid",uid);
//                    startActivity(intent);
//
//                }
                // ...
            } else {

                Log.d(TAG, "Login_Fail");

            }
        }
    }

    private void getSalonId(String owner_docId) {
        db.collection("Salon").whereEqualTo("owner_docId",owner_docId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size()>0){
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    String salonDocID = documentSnapshot.getId();

                    updateFcm(salonDocID);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updateFcm(String salonDocID) {
        db.collection("Salon").document(salonDocID).update("fcm_token",FCVToken).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.person_icon)      // Set logo drawable

                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_theme_logo]
    }

    public void privacyAndTerms() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();
        // [START auth_fui_pp_tos]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_pp_tos]
    }



    public void LoginCantPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View mylogpop = getLayoutInflater().inflate(R.layout.owner_login_wrong_popup,null);

        popup_try_btn = mylogpop.findViewById(R.id.try_btn_pop);
        popup_regi_btn = mylogpop.findViewById(R.id.regi_btn_pop);

        dialogBuilder.setView(mylogpop);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        popup_try_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        popup_regi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                intent.putExtra("email", email_fld.getText().toString());


                Log.d(TAG, "LoginOk_User_NotFound");
                startActivity(intent);
                finish();
            }
        });



    }
}