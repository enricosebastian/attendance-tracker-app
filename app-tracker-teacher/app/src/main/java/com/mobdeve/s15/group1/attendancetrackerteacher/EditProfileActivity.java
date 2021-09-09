package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = "EditProfileActivity";

    private SharedPreferences sp;

    private Uri imageUri = null;
    private EditText inputFirstName, inputLastname;
    private Button btnSelectImage, btnSaveEditProfile, btnCancelEditProfile;
    private ImageView img_profilePic; //DAT NAMING INCONSISTENCY THO
    private ProgressDialog progressDialog;
    private String email;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    try {
                        if(result.getData() != null) {
                            imageUri = result.getData().getData();
                            Picasso.get().load(imageUri).into(img_profilePic);
                            Log.d(TAG,"ImgUri is "+imageUri);
                        } else {
                            Log.d(TAG, "Null input");
                        }
                    } catch(Exception exception){
                        Log.d(TAG,""+exception.getLocalizedMessage());
                    }
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.sp = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);

        this.email = sp.getString(Keys.SP_EMAIL_KEY,"");

        this.inputFirstName         = findViewById(R.id.inputFirstName);
        this.inputLastname          = findViewById(R.id.inputLastName);
        this.img_profilePic         = findViewById(R.id.img_profilePic); //DAT NAMING INCONSISTENCY THO SMH shoulda been imgProfilePic good lawd sweet jesus

        this.btnSelectImage         = findViewById(R.id.btnSelectImage);
        this.btnSaveEditProfile     = findViewById(R.id.btnSaveEditProfile);
        this.btnCancelEditProfile   = findViewById(R.id.btnCancelEditProfile);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(EditProfileActivity.this);

        initializeViews(email);

        // When user selects an image
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
            }
        });

        //When user cancels editing the profile
        btnCancelEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //When user saves the things
        btnSaveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastname.getText().toString();
                updateAccount(firstName, lastName);
            }
        });
    }

    //Initializes the views of the edit profile activity
    protected void initializeViews(String email) {
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

        Db.getDocumentsWith(Db.COLLECTION_USERS,
            Db.FIELD_EMAIL, email).
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<DocumentSnapshot> results= Db.getDocuments(task);
                    String firstName = results.get(0).getString(Db.FIELD_FIRSTNAME);
                    String lastName = results.get(0).getString(Db.FIELD_LASTNAME);

                    inputFirstName.setText(firstName);
                    inputLastname.setText(lastName);

                    String documentId = Db.getIdFromTask(task);
                    Db.getProfilePic(documentId).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()) {
                                Uri imgUri = task.getResult();
                                Picasso.get().load(imgUri).into(img_profilePic);
                                progressDialog.setCanceledOnTouchOutside(true);
                                progressDialog.dismiss();
                            } else {
                                Log.d(TAG,"No profile image found. Switching to default");
                                progressDialog.setCanceledOnTouchOutside(true);
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            });
    }

    //Updates the account
    protected void updateAccount(String firstName, String lastName) {
        this.progressDialog.setMessage("Updating information...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);
        Db.getDocumentsWith(Db.COLLECTION_USERS,
            Db.EMAIL_FIELD, email).
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    //imgUri = Uri.parse("android.resource://com.mobdeve.s15.group1.attendancetrackerteacher/"+R.id.img_profilePic);
                    String documentId = Db.getIdFromTask(task);
                    Log.d(TAG,"id is "+documentId);

                    if(imageUri != null) {
                        Tasks.whenAllSuccess(Db.uploadImage(documentId, imageUri)).
                        addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Object>> task) {
                                Log.d(TAG,"Upload was successfully added to the drive");
                                Db.getCollection(Db.COLLECTION_USERS).document(documentId).
                                        update(Db.FIELD_FIRSTNAME, firstName, Db.FIELD_LASTNAME, lastName).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d(TAG, "Successfully updated db");
                                                progressDialog.setCanceledOnTouchOutside(true);
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfileActivity.this, "Changes saved!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                            }
                        });
                    } else { //if user does not edit their profile picture
                        Db.getCollection(Db.COLLECTION_USERS).document(documentId).
                                update(Db.FIELD_FIRSTNAME, firstName, Db.FIELD_LASTNAME, lastName).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "Successfully updated db");
                                        progressDialog.setCanceledOnTouchOutside(true);
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "Changes saved!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }
                }
            });
    }
}