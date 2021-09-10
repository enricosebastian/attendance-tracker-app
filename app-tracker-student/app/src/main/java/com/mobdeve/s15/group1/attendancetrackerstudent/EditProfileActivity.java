package com.mobdeve.s15.group1.attendancetrackerstudent;

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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    public static final String  TAG = "EditProfileActivity";

    private SharedPreferences   sp;

    private Uri                 imageUri = null;
    private EditText            inputFirstName,
                                inputLastname;
    private ImageView           img_profilePic;
    private Button              btnSelectImage,
                                btnSaveEditProfile,
                                btnCancelEditProfile;

    private String              email;
    private ProgressDialog      progressDialog;

    //catches if user successfully uploads a new profile image
    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    try {
                        if(result.getData() != null) {
                            Log.d(TAG, "hello");
                            imageUri = result.getData().getData();
                            Picasso.get().load(imageUri).into(img_profilePic);
                            Log.d(TAG,"ImgUri is "+imageUri);

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

        this.sp     = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.email  = sp.getString(Keys.SP_EMAIL_KEY,"");

        this.inputFirstName         = findViewById(R.id.inputFirstName);
        this.inputLastname          = findViewById(R.id.inputLastName);
        this.img_profilePic         = findViewById(R.id.img_profilePic);
        this.btnSelectImage         = findViewById(R.id.btnSelectImage);
        this.btnSaveEditProfile     = findViewById(R.id.btnSaveEditProfile);
        this.btnCancelEditProfile   = findViewById(R.id.btnCancelEditProfile);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(EditProfileActivity.this);

        //When user selects an image
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
                updateAccount(  inputFirstName.getText().toString(),
                                inputLastname.getText().toString()  );
            }
        });

        initializeViews();
    }

    //initializes views of activity
    protected void initializeViews() {
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

        //initializes input fields & profile pic in activity by searching the user DB
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> results = Db.getDocuments(task);

                inputFirstName.setText(results.get(0).getString(Db.FIELD_FIRSTNAME));
                inputLastname.setText(results.get(0).getString(Db.FIELD_LASTNAME));

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

        //updates user collection by finding document ID
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                String documentId = Db.getIdFromTask(task);

                //if user edits profile with image uploading
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
                                    Toast.makeText(EditProfileActivity.this, "Changes saved!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    //if user did not edit profile picture
                    Db.getCollection(Db.COLLECTION_USERS).document(documentId).
                    update(Db.FIELD_FIRSTNAME, firstName, Db.FIELD_LASTNAME, lastName).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Successfully updated db");
                            Toast.makeText(EditProfileActivity.this, "Changes saved!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

}