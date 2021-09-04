package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegistrationView extends AppCompatActivity {

    public final String TAG = "Registration";
    private static String USERNAME_STATE_KEY = "USERNAME_KEY";
    private static String EMAIL_STATE_KEY = "EMAIL_KEY";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private EditText    inputFirstName,
                        inputLastName,
                        inputEmail,
                        inputPassword,
                        inputIdNumber;

    private Button btnSubmit, btnCancelRegistration;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        //get firestore instance
        this.db = FirebaseFirestore.getInstance();

        this.btnSubmit = findViewById(R.id.btnSubmit);
        this.btnCancelRegistration = findViewById(R.id.btnCancelRegistration);
        this.inputFirstName = findViewById(R.id.inputFirstName);
        this.inputLastName = findViewById(R.id.inputLastName);
        this.inputEmail = findViewById(R.id.inputEmail);
        this.inputPassword = findViewById(R.id.inputPassword);
        this.inputIdNumber = findViewById(R.id.inputIdNumber);

        // When User registers
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String firstName = inputFirstName.getText().toString();
                String idNumber = inputIdNumber.getText().toString();
                String lastName = inputLastName.getText().toString();
                String password = inputPassword.getText().toString();
                String userType = "teacher";

                // If not all entries are filled
                if(!doAllFieldsHaveEntries()) {
                    Log.d(TAG, "Not all fields have entries");
                    Toast.makeText(getApplicationContext(), "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    // Get the user collection
                    Db.getUsersCollectionReference().
                        whereEqualTo(Db.EMAIL_FIELD, email).
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot emailQuery = task.getResult();
                            List<DocumentSnapshot> emailResult = emailQuery.getDocuments();

                            //If email is unique
                            if(emailResult.isEmpty()) {

                                Db.getUsersCollectionReference().
                                    whereEqualTo(Db.IDNUMBER_FIELD, idNumber).
                                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot idNumberQuery = task.getResult();
                                        List<DocumentSnapshot> idNumberResult = idNumberQuery.getDocuments();

                                        // If id number is unique
                                        if(idNumberResult.isEmpty()) {
                                            Map<String, Object> input = new HashMap<>();
                                            input.put(Db.EMAIL_FIELD,email);
                                            input.put(Db.FIRSTNAME_FIELD,firstName);
                                            input.put(Db.IDNUMBER_FIELD,idNumber);
                                            input.put(Db.LASTNAME_FIELD,lastName);
                                            input.put(Db.PASSWORD_FIELD,password);
                                            input.put(Db.USERTYPE_FIELD,userType);

                                            Db.
                                                getUsersCollectionReference().
                                                add(input).
                                                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "Input added succesfully");
                                                        Intent intent = new Intent(RegistrationView.this, ClasslistView.class);
                                                        intent.putExtra(EMAIL_STATE_KEY,email);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        String error = e.getMessage();
                                                        Log.w(TAG, "Error adding document: "+ error);
                                                    }
                                                });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Account already exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Account already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

    /*  This method was created for code readability. It checks if the fields have completely been
     *  filled up by the user
     **/
    private boolean doAllFieldsHaveEntries() {
        return  !inputEmail.getText().toString().isEmpty() &&
                !inputIdNumber.getText().toString().isEmpty() &&
                !inputLastName.getText().toString().isEmpty() &&
                !inputPassword.getText().toString().isEmpty();
    }
}