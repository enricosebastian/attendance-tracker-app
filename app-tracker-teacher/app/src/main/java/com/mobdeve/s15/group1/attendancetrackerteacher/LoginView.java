package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginView extends AppCompatActivity {
    private final String TAG = "LoginView.java";

    private static String SP_FILE_NAME = "LoginPreferences";
    private static String USERNAME_STATE_KEY = "USERNAME_KEY";
    private static String EMAIL_STATE_KEY = "EMAIL_KEY";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnLogin = findViewById(R.id.btnLogin);
        this.btnRegister = findViewById(R.id.btnRegister);
        this.inputEmail = findViewById(R.id.inputEmail);
        this.inputPassword = findViewById(R.id.inputPassword);

        this.sp = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();

        this.db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                FirestoreReferences.getUsersCollectionReference()
                        .whereEqualTo(FirestoreReferences.EMAIL_FIELD, email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(task.getResult().isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "dude doesnt exist lmao", Toast.LENGTH_SHORT).show();
                                    } else {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        List<DocumentSnapshot> result = querySnapshot.getDocuments();
                                        if(result.get(0).get(FirestoreReferences.USERTYPE_FIELD).toString().equals("teacher")) {
                                            if(result.get(0).get(FirestoreReferences.PASSWORD_FIELD).toString().equals(password)) {
                                                Intent intent = new Intent(LoginView.this, ClasslistView.class);

                                                intent.putExtra(EMAIL_STATE_KEY,result.get(0).get(FirestoreReferences.EMAIL_FIELD).toString());
                                                intent.putExtra(USERNAME_STATE_KEY,result.get(0).get(FirestoreReferences.USERNAME_FIELD).toString());
                                                startActivity(intent);

                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                                            };
                                        } else {
                                            Toast.makeText(getApplicationContext(), "User is not a teacher", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Log.d(TAG,"Error getting Users document: "+task.getException());
                                }
                        }
                    });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, RegisterTeacherActivity.class);
                startActivity(intent);
            }
        });
    }
}