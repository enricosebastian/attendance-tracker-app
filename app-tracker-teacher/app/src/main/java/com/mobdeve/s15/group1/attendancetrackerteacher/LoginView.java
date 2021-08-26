package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginView extends AppCompatActivity {
    private final String TAG = "LoginView.java";

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

        this.db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                CollectionReference collectionRef = db.collection(FirestoreReferences.USERS_COLLECTION);
                
                Query query = collectionRef.whereEqualTo(FirestoreReferences.EMAIL_FIELD, email);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "dude doesnt exist lmao", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "dude does exist lmao", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG,"Error getting Users document: "+task.getException());
                        }
                    }
                });

//                collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(queryDocumentSnapshots.isEmpty()) {
//                            Toast.makeText(getApplicationContext(), "this db is empty", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "this db is not empty", Toast.LENGTH_SHORT).show();
//                            Log.d("collect database ","size is "+queryDocumentSnapshots.size());
//                        }
//                    }
//                });
//
//                Intent intent = new Intent(LoginView.this, ClasslistView.class);
//                startActivity(intent);
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