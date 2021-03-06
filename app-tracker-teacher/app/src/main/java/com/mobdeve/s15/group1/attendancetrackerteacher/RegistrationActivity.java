package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
    Register requires a First name, Last name, valid email, a password, and an ID number.
    All of these fields must be filled up, and the ID number must be of 8-length numerical value.
 */
public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";

    //shared pref initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    //widget initialization
    private TextView    tvEmailFormatError;
    private EditText    inputFirstName,
                        inputLastName,
                        inputEmail,
                        inputPassword,
                        inputIdNumber;
    private Button      btnSubmit,
                        btnCancelRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);


        this.sp = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();

        this.btnSubmit                  = findViewById(R.id.btnSubmit);
        this.btnCancelRegistration      = findViewById(R.id.btnCancelRegistration);
        this.inputFirstName             = findViewById(R.id.inputFirstName);
        this.inputLastName              = findViewById(R.id.inputLastName);
        this.inputEmail                 = findViewById(R.id.inputEmail);
        this.inputPassword              = findViewById(R.id.inputPassword);
        this.inputIdNumber              = findViewById(R.id.inputIdNumber);

        // When User submits registration
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email        = inputEmail.getText().toString().toLowerCase().trim();
                String firstName    = inputFirstName.getText().toString().trim();
                String idNumber     = inputIdNumber.getText().toString().trim();
                String lastName     = inputLastName.getText().toString().trim();
                String password     = inputPassword.getText().toString();

                // "If not all entries are filled"
                if(!doAllFieldsHaveEntries() && isEmailValid(email)) {
                    Log.d(TAG, "Not all fields have entries");
                    Toast.makeText(getApplicationContext(), "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    //checks id for BOTH teachers and students, just in case
                    
                    // if the ID number is a number, and is 8 length, and
                    if(isIDNumberValid(idNumber) && isEmailValid(email)) {
                        // Check if email exists
                        Db.getDocumentsWith(Db.COLLECTION_USERS,
                                Db.FIELD_EMAIL, email).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<DocumentSnapshot> emailresult = Db.getDocuments(task);

                                    //If the email does not exist in the db yet
                                    if(emailresult.size() == 0) {
                                        Db.getDocumentsWith(Db.COLLECTION_USERS,
                                                Db.FIELD_IDNUMBER, idNumber).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                List<DocumentSnapshot> result = Db.getDocuments(task);
                                                if (result.size() == 0) {
                                                    createNewUser(email, firstName, idNumber, lastName, password);
                                                    editor.putString(Keys.SP_EMAIL_KEY, email);
                                                    editor.putString(Keys.SP_USERTYPE_KEY, "teacher");
                                                    editor.commit();

                                                    Intent intent = new Intent(RegistrationActivity.this, CourseListActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Account already exists.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Account already exists! Use another email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "One of the fields has a wrong format!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // When user cancels the registration
        btnCancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isEmailValid(CharSequence email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //This method checks the length of the id number entered by the student

    private boolean isIDNumberValid(String val) {
        boolean isNumber = false, isLength8 = false;

        if(val.length() == 8)
            isLength8 = true;

        try {
            int num = Integer.parseInt(val);
            isNumber = true;
        } catch(NumberFormatException e) {
            Log.d(TAG, "ID number field is a string, not a number");
        }
        return isNumber && isLength8;
    }

    /**  This method was created for code readability. It checks if the fields have completely been
     *  filled up by the user
     */
    private boolean doAllFieldsHaveEntries() {
        return  !inputEmail.getText().toString().isEmpty() &&
                !inputIdNumber.getText().toString().isEmpty() &&
                !inputLastName.getText().toString().isEmpty() &&
                !inputPassword.getText().toString().isEmpty();
    }

    // This method performs the db operation that adds the user to the app given their inputs
    protected void createNewUser(String email, String firstName, String idNumber, String lastName, String password) {
        Map<String, Object> input = new HashMap<>();

        input.put(Db.FIELD_EMAIL, email);
        input.put(Db.FIELD_FIRSTNAME, firstName);
        input.put(Db.FIELD_IDNUMBER, idNumber);
        input.put(Db.FIELD_LASTNAME, lastName);
        input.put(Db.FIELD_PASSWORD, password);
        input.put(Db.FIELD_USERTYPE, "teacher");

        Db.addDocument(Db.COLLECTION_USERS, input);

        Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
    }
}