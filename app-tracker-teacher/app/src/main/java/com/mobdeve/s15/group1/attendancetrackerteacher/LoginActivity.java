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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginView.java";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister;
    private static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnLogin = findViewById(R.id.btnLogin);
        this.btnRegister = findViewById(R.id.btnRegister);
        this.inputEmail = findViewById(R.id.inputEmail);
        this.inputPassword = findViewById(R.id.inputPassword);

        this.sp = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();
        this.email = sp.getString(Keys.SP_EMAIL_KEY, "");

        //initializing shared preference file
        if(!email.isEmpty()) {
            //login screen
            Intent intent = new Intent(LoginActivity.this, ClasslistActivity.class);
            intent.putExtra(Keys.SP_EMAIL_KEY,email);
            startActivity(intent);
            finish();
        }


        //logging in
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
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


        //registering a new account
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


    protected void login(String email, String password) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                if(!result.isEmpty()) {
                    if(result.get(0).getString(Db.FIELD_USERTYPE).equals("student")) {
                        Log.d(TAG,"Student tried logging in");
                        Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();
                    } else if (result.get(0).getString(Db.FIELD_PASSWORD).equals(password)) {
                        Log.d(TAG,"User exists");
                        editor.putString(Keys.SP_EMAIL_KEY, email);
                        editor.putString(Keys.SP_USERTYPE_KEY, result.get(0).getString(Db.FIELD_USERTYPE));
                        editor.commit();
                        Intent loginIntent = new Intent(LoginActivity.this, ClasslistActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        Log.d(TAG,"Incorrect password entry");
                        Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG,"Account does not exist");
                    Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}