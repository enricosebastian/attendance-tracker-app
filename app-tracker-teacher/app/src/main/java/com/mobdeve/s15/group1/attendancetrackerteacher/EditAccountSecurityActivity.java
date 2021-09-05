package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EditAccountSecurityActivity extends AppCompatActivity {

    public static final String TAG = "EditAccountSecurity";

    private SharedPreferences sp;

    private Uri imageUri = null;
    private EditText inputOldPassword, inputNewPassword, inputConfirmNewPassword;
    private Button btnSave, btnCancel;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_security);

        this.sp                         = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.email                      = sp.getString(Keys.SP_EMAIL_KEY,"");

        this.inputOldPassword           = findViewById(R.id.inputOldPassword);
        this.inputNewPassword           = findViewById(R.id.inputNewPassword);
        this.inputConfirmNewPassword    = findViewById(R.id.inputConfirmNewPassword);
        this.btnSave                    = findViewById(R.id.btnSave);
        this.btnCancel                  = findViewById(R.id.btnCancel);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = inputOldPassword.getText().toString();
                String newPassword = inputNewPassword.getText().toString();
                String confirmNewPassword = inputConfirmNewPassword.getText().toString();
                
                if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Entry field is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    verifyLoginCredentials(oldPassword, newPassword, confirmNewPassword);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void verifyLoginCredentials(String oldPassword, String newPassword, String confirmNewPassword) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
            Db.FIELD_EMAIL, email,
            Db.FIELD_PASSWORD, oldPassword).
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<DocumentSnapshot> result = Db.getDocuments(task);
                    if(result.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                    } else {
                        if(newPassword.equals(confirmNewPassword)) {
                            updateLoginCredentials(newPassword);
                        } else {
                            Toast.makeText(getApplicationContext(), "New password does not match!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    protected void updateLoginCredentials(String newPassword) {
        Db.getDocumentsWith(Db.COLLECTION_USERS, Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String documentId = Db.getIdFromTask(task);
                Db.getCollection(Db.COLLECTION_USERS).document(documentId).
                update(Db.FIELD_PASSWORD, newPassword).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG,"Successfully changed password");
                        Toast.makeText(getApplicationContext(), "Successfully changed password!", Toast.LENGTH_SHORT).show();
                        inputOldPassword.setText("");
                        inputNewPassword.setText("");
                        inputConfirmNewPassword.setText("");
                    }
                });
            }
        });
    }
}