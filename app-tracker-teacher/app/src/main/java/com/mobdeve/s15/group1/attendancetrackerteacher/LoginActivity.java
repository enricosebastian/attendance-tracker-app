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
    private final String TAG = "LoginView.java";
    private static String SP_FILE_NAME = "LoginPreferences";
    private static String SP_EMAIL_KEY = "SP_EMAIL_KEY";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister;


    //junk shit to delete later
    private static String SP_USERNAME_KEY = "SP_USERNAME_KEY"; //delete
    private static String USERNAME_STATE_KEY = "USERNAME_KEY"; //delete
    private static String EMAIL_STATE_KEY = "EMAIL_KEY"; //delete
    ///////////////junk shit to delete later

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

        String previousEmailEntry = sp.getString(SP_EMAIL_KEY, "");
        String previousUsernameEntry = sp.getString(SP_USERNAME_KEY, ""); //delete

        Log.d(TAG,"shared preferences: "+previousEmailEntry); //delete

        if(!previousEmailEntry.isEmpty()) {
            //login screen
            Intent intent = new Intent(LoginActivity.this, ClasslistView.class);
            intent.putExtra(SP_EMAIL_KEY,previousEmailEntry);
            startActivity(intent);
            finish();
        }

//        Db.getDocumentsWith("email","ben@dlsu.edu.ph","password","benpassword",Db.USERS_COLLECTION).
//            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    List<DocumentSnapshot> result = Db.getDocuments(task);
//                    Log.d(TAG,"result is "+result.size());
//                }b
//            });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                Db.getDocumentsWith(Db.USERS_COLLECTION, "email", email,"password",password).
                        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<DocumentSnapshot> result = Db.getDocuments(task);
                                Log.d(TAG,"result is "+result.get(0).get("userType").toString()); //cleanup lmao
                            }
                        });

//                Task<QuerySnapshot> querySnapshot = Db.getUserInfoFromEmail(email);
//                querySnapshot.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<DocumentSnapshot> result = Db.toList(task);
//                        DocumentSnapshot documentSnapshot = Db.getFirstResult(task);
//                        if(result.isEmpty() || !documentSnapshot.get("userType").equals("teacher")) {
//                            Toast.makeText(getApplicationContext(), "doesnt exist bro lmao", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if(documentSnapshot.get("password").toString().equals(password)) {
//                                Intent intent = new Intent(LoginActivity.this, ClasslistView.class);
//
//                                intent.putExtra(EMAIL_STATE_KEY,documentSnapshot.get(Db.EMAIL_FIELD).toString());
//                                intent.putExtra(USERNAME_STATE_KEY,documentSnapshot.get(Db.USERNAME_FIELD).toString());
//
//                                editor.putString(SP_EMAIL_KEY,documentSnapshot.getString(Db.EMAIL_FIELD));
//                                editor.putString(SP_USERNAME_KEY,documentSnapshot.getString(Db.USERNAME_FIELD));
//                                editor.commit();
//
//                                startActivity(intent);
//                                finish(); //ends login activity
//                            } else {
//                                Toast.makeText(getApplicationContext(), "wrong password!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }
//                });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationView.class);
                startActivity(intent);
            }
        });

        //delete
        if(!previousEmailEntry.equals("") && !previousUsernameEntry.equals("") ) {
            Intent intent = new Intent(LoginActivity.this, ClasslistView.class);

            intent.putExtra(EMAIL_STATE_KEY,previousEmailEntry);
            intent.putExtra(USERNAME_STATE_KEY,previousUsernameEntry);

            startActivity(intent);
            finish(); //ends login activity
        }
        //////////////////////delete
    }
}