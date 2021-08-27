package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditProfile extends AppCompatActivity {

    private static String SP_FILE_NAME = "LoginPreferences";
    private static String SP_EMAIL_KEY = "SP_EMAIL_KEY";
    private static String SP_USERNAME_KEY = "SP_USERNAME_KEY";

    EditText inputPassword, inputFirstName, inputLastname, inputEmail;
    Button btnSelectImage, btnSaveEditProfile, btnCancelEditProfile;

    private static String previousUsernameEntry;

    private Uri imageUri = null;

    private SharedPreferences sp;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {

                                Query q = FirestoreReferences.findDocuments(FirestoreReferences.USERS_COLLECTION,"username",previousUsernameEntry);
                                q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        String id = FirestoreReferences.getIdFromTask(task);

                                        imageUri = result.getData().getData();
                                        Task uploadTask = FirestoreReferences.uploadImage(id, imageUri);
                                        Tasks.whenAllSuccess(uploadTask).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                            @Override
                                            public void onSuccess(List<Object> objects) {
                                                Toast.makeText(getApplicationContext(), "Upload was a success", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });


                            }
                        } catch(Exception exception){
                            Log.d("TAG",""+exception.getLocalizedMessage());
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.sp = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);

        this.previousUsernameEntry = sp.getString(SP_USERNAME_KEY, "");

        this.inputPassword = findViewById(R.id.inputPassword);
        this.inputFirstName = findViewById(R.id.inputFirstName);
        this.inputLastname = findViewById(R.id.inputLastName);
        this.inputEmail = findViewById(R.id.inputEmail);

        this.btnSelectImage = findViewById(R.id.btnSelectImage);
        this.btnSaveEditProfile = findViewById(R.id.btnSaveEditProfile);
        this.btnCancelEditProfile = findViewById(R.id.btnCancelEditProfile);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
            }
        });

        btnCancelEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "Changes saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}