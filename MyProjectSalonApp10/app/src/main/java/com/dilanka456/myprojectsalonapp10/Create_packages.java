package com.dilanka456.myprojectsalonapp10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dilanka456.myprojectsalonapp10.Model.Category;
import com.dilanka456.myprojectsalonapp10.Model.Package;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Create_packages extends AppCompatActivity {

    private static final int FILE_CHOOSE_PROFILE_PIC_ACTIVITY_RESULT_CODE = 120;
    ImageView category_add_btn, package_img_view;
    Spinner category_spinner, age_fld;
    EditText duration_fld, price_fld, name_fld, description_fld,discount_fld;
    Button save_btn, remove_btn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter categoryArrayAdapter;
    private Uri selectedCategoryImageURI;
    private StorageReference storageReference;
    private String downloadImageUrl;
    private String downUrl;
    boolean packFound;
    private String salonDocId;
    private String packageDocId;
    private ArrayAdapter agesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_packages);

        storageReference = FirebaseStorage.getInstance().getReference();

//        ---------------------------------------get Existing Data -------------------------------------
        Bundle extras = getIntent().getExtras();
        packFound = (boolean) extras.get("packFound");
        salonDocId = extras.getString("salonDocId");




//        ---------------------------Assign Fields----------------------------------
        package_img_view = findViewById(R.id.package_image_add_fld);
        category_spinner = findViewById(R.id.package_category_spinner);
        category_add_btn = findViewById(R.id.category_add_btn);
        age_fld = findViewById(R.id.package_age_fld);
        duration_fld = findViewById(R.id.package_duration_fld);
        price_fld = findViewById(R.id.package_price_fld);
        save_btn = findViewById(R.id.package_save_btn);
        remove_btn = findViewById(R.id.package_remove_btn);
        name_fld = findViewById(R.id.package_name_fld);
        description_fld = findViewById(R.id.package_description_fld);





        if (packFound) {
            packageDocId = extras.getString("packageId");

            db.collection("Package").document(packageDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        Package aPackage = documentSnapshot.toObject(Package.class);

                        name_fld.setText(aPackage.getName());
                        description_fld.setText(aPackage.getDescription());

                        duration_fld.setText(aPackage.getDuration());
                        price_fld.setText(String.valueOf(aPackage.getPrice()));
                        Picasso.get().load(aPackage.getImg_url()).into(package_img_view);


                    }else{

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

//        -----------------------------------image btn action--------------------------
        package_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileChooser = new Intent();
                fileChooser.setAction(Intent.ACTION_GET_CONTENT);
                fileChooser.setType("image/*"); // optional
                startActivityForResult(Intent.createChooser(fileChooser, "Select File"), FILE_CHOOSE_PROFILE_PIC_ACTIVITY_RESULT_CODE);
            }
        });


        String[] ages = {"Child male", "Child female", "Men", "Women"};
         agesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ages);
        age_fld.setAdapter(agesAdapter);

        ArrayList<String> categoryArray = new ArrayList<>();


//        ----------------------------------categories add to fld----------------------------------------------
        db.collection("Category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot ds : documents) {
                        Category category = ds.toObject(Category.class);
                        categoryArray.add(category.getCategory());
                    }
                    categoryArrayAdapter = new ArrayAdapter(Create_packages.this, android.R.layout.simple_spinner_item, categoryArray);
                    category_spinner.setAdapter(categoryArrayAdapter);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        category_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MysalonListPopup();
            }
        });


        //        --------------------------------save btn action-----------------------------
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                packageAndImageUpload();


            }
        });

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Package").document(packageDocId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Create_packages.this, "Package Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(Create_packages.this,Package_list.class);
                        intent.putExtra("salonDocId",salonDocId);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Create_packages.this, "package delete Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


    public void packageAndImageUpload() {
        String package_img_name = "Package_img/" + name_fld.getText().toString() + System.currentTimeMillis() + ".png";
        StorageReference filePath = storageReference.child(package_img_name);

        final UploadTask uploadTask = filePath.putFile(selectedCategoryImageURI);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        downUrl = task.getResult().toString();

                        Package packagenew = new Package();
                        packagenew.setName(name_fld.getText().toString());
                        packagenew.setDescription(description_fld.getText().toString());
                        packagenew.setCategory(category_spinner.getSelectedItem().toString());
                        packagenew.setAge(age_fld.getSelectedItem().toString());
                        packagenew.setDuration(duration_fld.getText().toString());
                        packagenew.setPrice(price_fld.getText().toString());
                        packagenew.setImg_url(downUrl);
                        packagenew.setSalon_doc_id(salonDocId);

                        if (packFound){

                            Map<String,Object> map = new HashMap<>();
                            map.put("name",name_fld.getText().toString());
                            map.put("description",description_fld.getText().toString());
                            map.put("category", category_spinner.getSelectedItem().toString());
                            map.put("age", age_fld.getSelectedItem().toString());
                            map.put("duration",duration_fld.getText().toString());
                            map.put("price",price_fld.getText().toString());
                            map.put("img_url",downUrl);


                            db.collection("Package").document(packageDocId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Create_packages.this, "Package Updated...!", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(Create_packages.this,Package_list.class);
                                    intent.putExtra("salonDocId",salonDocId);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Create_packages.this, "Package Update Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{

                            db.collection("Package").add(packagenew).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Create_packages.this, "Package Saved", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(Create_packages.this,Package_list.class);
                                    intent.putExtra("salonDocId",salonDocId);
                                    startActivity(intent);
                                    finish();

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
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FILE_CHOOSE_PROFILE_PIC_ACTIVITY_RESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                selectedCategoryImageURI = data.getData();

                Picasso.get().load(selectedCategoryImageURI).into(package_img_view);

            } else {
                Toast.makeText(this, "Pro Pic File not Selected : " + resultCode, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void MysalonListPopup() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View myListVIew = getLayoutInflater().inflate(R.layout.category_create_pop, null);

        Button close_btn = myListVIew.findViewById(R.id.category_pop_close_btn);
        Button save_btn = myListVIew.findViewById(R.id.category_pop_save_btn);
        EditText category_add_fld = myListVIew.findViewById(R.id.new_category_fld);


        dialogBuilder.setView(myListVIew);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                category.setCategory(category_add_fld.getText().toString());

                db.collection("Category").add(category).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Create_packages.this, "Category Added", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Create_packages.this, "new Category Add Fail:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }
}