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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private static final String         TAG = "RegistrationActivity";

    //shared pref initialization
    private SharedPreferences           sp;
    private SharedPreferences.Editor    editor;

    //widget initialization
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


        // When User registers
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email        = inputEmail.getText().toString().toLowerCase().trim();
                String firstName    = inputFirstName.getText().toString().trim();
                String idNumber     = inputIdNumber.getText().toString().trim();
                String lastName     = inputLastName.getText().toString().trim();
                String password     = inputPassword.getText().toString();

                // If not all entries are filled
                if(!doAllFieldsHaveEntries()) {
                    Log.d(TAG, "Not all fields have entries");
                    Toast.makeText(getApplicationContext(), "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    //checks id for BOTH teachers and students, just in case

                    // if the ID number is a number, and is 8 length
                    if(isIDNumberValid(idNumber)) {
                        Db.getDocumentsWith(Db.COLLECTION_USERS,
                                Db.FIELD_EMAIL, email).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<DocumentSnapshot> emailresult = Db.getDocuments(task);

                                //If the email does not exist in the db yet
                                if(emailresult.size() == 0) {
                                    Db.getDocumentsWith(Db.COLLECTION_USERS,
                                            Db.FIELD_IDNUMBER, idNumber).
                                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    List<DocumentSnapshot> result = Db.getDocuments(task);
                                                    if(result.size() == 0) {
                                                        //that means this dude is unique/no user like this exists, so they can create
                                                        createNewUser(email, firstName, idNumber, lastName, password);
                                                        editor.putString(Keys.SP_EMAIL_KEY, email);
                                                        editor.putString(Keys.SP_USERTYPE_KEY, "student");
                                                        editor.commit();

                                                        Intent getCourseListActivityIntent = new Intent(RegistrationActivity.this, CourseListActivity.class);
                                                        startActivity(getCourseListActivityIntent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Account exists already!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Account already exists! Use another email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong ID number format!", Toast.LENGTH_SHORT).show();
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

    //extra validation for id number
    private boolean isIDNumberValid(String val) {
        boolean isNumber = false, isLength8 = false;

        if(val.length() == 8) isLength8 = true;

        try {
            int num = Integer.parseInt(val);
            isNumber = true;
        } catch(NumberFormatException e) {
            Log.d(TAG, "ID number field is a string, not a number");
        }
        return isNumber && isLength8;
    }

    /*  This method was created for code readability. It checks if the fields have completely been
     *  filled up by the user
     **/

    private boolean doAllFieldsHaveEntries() {
        return  !inputEmail.getText().toString().isEmpty() &&
                !inputIdNumber.getText().toString().isEmpty() &&
                !inputLastName.getText().toString().isEmpty() &&
                !inputPassword.getText().toString().isEmpty();
    }

    //if entries are valid, place data into a hashmap, and send it to db
    protected void createNewUser(String email, String firstName, String idNumber, String lastName, String password) {
        Map<String, Object> input = new HashMap<>();

        input.put(Db.FIELD_EMAIL, email);
        input.put(Db.FIELD_FIRSTNAME, firstName);
        input.put(Db.FIELD_IDNUMBER, idNumber);
        input.put(Db.FIELD_LASTNAME, lastName);
        input.put(Db.FIELD_PASSWORD, password);
        input.put(Db.FIELD_USERTYPE, "student");

        Db.addDocument(Db.COLLECTION_USERS, input);

        Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
    }
}