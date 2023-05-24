package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dilanka456.myprojectcustomer10.Model.Customer;
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

public class Login_activity extends AppCompatActivity {
    private static final String TAG = "LoginTag :";
    EditText email_fld, password_fld;
    CheckBox showBox;
    Button login_btn, google_btn;
    TextView forget_pw_lbl;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int RC_SIGN_IN = 123;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private String FCVToken;
    private String customerDocId;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_avtivity);

        initeFCM();

        sp = PreferenceManager.getDefaultSharedPreferences(Login_activity.this);
        editor = sp.edit();

//        ---------------------------Assign Field--------------------------------------------------------------------
        email_fld = findViewById(R.id.login_email_fld);
        password_fld = findViewById(R.id.login_password_fld);
        showBox = findViewById(R.id.login_show_check);

        login_btn = findViewById(R.id.login_login_btn);
        google_btn = findViewById(R.id.login_google_login_btn);
        forget_pw_lbl = findViewById(R.id.login_forgot_lbl);

//        ----------------------------------------forgot pw lbl Action--------------------------------------------------
        forget_pw_lbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this, Forget_password.class);
                startActivity(intent);
            }
        });

//        ------------------------------------------check box design code---------------------------------------------------------------
        showBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password_fld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password_fld.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
//        ------------------------------------end check box design code-----------------------------------------------------------


//        -------------------------------------Login Button Event------------------------------------------------------------

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Google eken nathuwa email Password gaha log wena wita local db eken che kirima mehi damiya yuthuya

                db.collection("Customer").whereEqualTo("email",email_fld.getText().toString()).whereEqualTo("password",password_fld.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.getDocuments().size()>0){

//                            User Found

                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Customer customer = documentSnapshot.toObject(Customer.class);
                            String CustomerDocId = documentSnapshot.getId();

                             Intent intent = new Intent(Login_activity.this,Home.class);
                             intent.putExtra("name",customer.getName());
                             intent.putExtra("email",customer.getEmail());
                             intent.putExtra("docId",CustomerDocId);
                             startActivity(intent);
                             finish();

                        }else {
//                            user Not Found
                            LoginCantPopup();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"Login_Error:"+e.getMessage());
                    }
                });


            }
        });

//        -------------------------------------------End login btn Event-----------------------------------------------------


//        ------------------------------------------Google Button Event--------------------------------------------------------
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent();
            }
        });


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


    //    ------------------------------------------------------Google Auth Codes-----------------------------------
    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(

                new AuthUI.IdpConfig.GoogleBuilder().build());

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
                String name = user.getDisplayName();
                String email = user.getEmail();
                String uid = user.getUid();


                db.collection("Customer").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            //User Found
                            Log.d(TAG, "User Found And Login Success....!!!!");

                            customerDocId = documentSnapshot.getId();

                            updatFCMToken(customerDocId);

                            saveEmailInPhone(email);

                            Customer customer = documentSnapshot.toObject(Customer.class);
                            Intent intent = new Intent(Login_activity.this, Home.class);
                            intent.putExtra("name", customer.getName());
                            intent.putExtra("email", customer.getEmail());
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish();

                        } else {
                            //User Not Found
                            Intent intent = new Intent(Login_activity.this, SignIn.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish();
                            Log.d(TAG, "User Not Fount:");

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Login Error:" + e.getMessage());
                        Toast.makeText(Login_activity.this, "Login Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//                boolean isRegistered = false;
//                if (!isRegistered){
//                    Toast.makeText(this, "Login_ok", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Login_activity.this,SignIn.class);
//                    intent.putExtra("name",name);
//                    intent.putExtra("email", email);
//                    intent.putExtra("uid",uid);
//                    startActivity(intent);
//
//                }

                // ...
            } else {
                //SignIn Fail
            }
        }
    }

    private void saveEmailInPhone(String email) {
        editor.putString("customerEmail", email);
        editor.commit();
    }

    private void updatFCMToken(String customerDocId) {
        db.collection("Customer").document(customerDocId).update("fcm_token",FCVToken).addOnSuccessListener(new OnSuccessListener<Void>() {
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
//                        .setTheme(R.style.MySuperAppTheme)      // Set theme
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
        final View mylogpop = getLayoutInflater().inflate(R.layout.login_wrong_popup,null);

         Button try_btn = mylogpop.findViewById(R.id.login_pop_try_btn);
         Button regi_btn = mylogpop.findViewById(R.id.login_pop_regi_btn);

        dialogBuilder.setView(mylogpop);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        try_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_fld.setText(null);
                password_fld.setText(null);
                alertDialog.cancel();
            }
        });

        regi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this, SignIn.class);
                intent.putExtra("email", email_fld.getText().toString());
                intent.putExtra("uid", "Manual Login");
                startActivity(intent);
            }
        });


    }



}