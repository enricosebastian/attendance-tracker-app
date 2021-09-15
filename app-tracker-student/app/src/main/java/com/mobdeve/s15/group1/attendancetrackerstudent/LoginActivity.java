package com.mobdeve.s15.group1.attendancetrackerstudent;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
/*
    Launching the app for the very first time OR logging out of the
    current session will launch the Login activity. It prompts for an email
    and a password for logging in, or a button to register a new account instead.
 */
public class LoginActivity extends AppCompatActivity {
    private final String                TAG = "LoginActivity.java";

    private SharedPreferences           sp;
    private SharedPreferences.Editor    editor;

    // widget initialization
    private EditText                    inputEmail,
                                        inputPassword;
    private Button                      btnLogin,
                                        btnRegister;
    private static String               email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.btnLogin           = findViewById(R.id.btnLogin);
        this.btnRegister        = findViewById(R.id.btnRegister);
        this.inputEmail         = findViewById(R.id.inputEmail);
        this.inputPassword      = findViewById(R.id.inputPassword);

        this.sp                 = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor             = sp.edit();
        this.email              = sp.getString(Keys.SP_EMAIL_KEY, "");


        //initializing shared preference file
        if(!email.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
            intent.putExtra(Keys.SP_EMAIL_KEY,email);
            startActivity(intent);
            finish();
        }

        //if user clicks login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().toLowerCase().trim();
                String password = inputPassword.getText().toString();
                if(email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email field is empty!", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password field is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    login(email, password);
                }
            }
        });

        //if user instead wants to click the register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationActivityIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registrationActivityIntent);
            }
        });

    }

    //login method with verification and validation functionality
    protected void login(String email, String password) {

        //checks if email already exists or not
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);

                if(!result.isEmpty()) {
                    if(result.get(0).getString(Db.FIELD_USERTYPE).equals("teacher") || result.get(0).getString(Db.FIELD_USERTYPE).equals("admin")) {
                        //if a student logged in
                        Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();
                    } else if (result.get(0).getString(Db.FIELD_PASSWORD).equals(password)) {
                        //if entire entry was correct
                        Log.d(TAG,"User exists.");

                        editor.putString(Keys.SP_EMAIL_KEY, email);
                        editor.putString(Keys.SP_USERTYPE_KEY, result.get(0).getString(Db.FIELD_USERTYPE));
                        editor.commit();

                        Intent loginIntent = new Intent(LoginActivity.this, CourseListActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        //if password entry was wrong
                        Log.d(TAG,"Incorrect password entry");
                        Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //if account does not exist
                    Log.d(TAG,"Account does not exist");
                    Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}