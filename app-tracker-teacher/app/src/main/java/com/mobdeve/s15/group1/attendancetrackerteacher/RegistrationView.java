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
                        inputUsername,
                        inputPassword,
                        inputIdNumber;

    private Button btnSubmit, btnCancelRegistration;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        this.db = FirebaseFirestore.getInstance();

        this.btnSubmit = findViewById(R.id.btnSubmit);
        this.btnCancelRegistration = findViewById(R.id.btnCancelRegistration);
        this.inputFirstName = findViewById(R.id.inputFirstName);
        this.inputLastName = findViewById(R.id.inputLastName);
        this.inputEmail = findViewById(R.id.inputEmail);
        this.inputUsername = findViewById(R.id.inputUsername);
        this.inputPassword = findViewById(R.id.inputPassword);
        this.inputIdNumber = findViewById(R.id.inputIdNumber);

        // When Clicks register
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String firstName = inputFirstName.getText().toString();
                String idNumber = inputIdNumber.getText().toString();
                String lastName = inputLastName.getText().toString();
                String password = inputPassword.getText().toString();
                String usertype = "teacher";
                String username = inputUsername.getText().toString();

                // If not all entries are field
                if(!doAllFieldsHaveEntries()) {
                    Log.d(TAG, "Not all fields have entries");
                    Toast.makeText(getApplicationContext(), "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * Todo: Check for unique ID NUMBER
                     * */
                
                    Db.getUsersCollectionReference().
                        whereEqualTo(Db.EMAIL_FIELD, email)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot emailQuery = task.getResult();
                            List<DocumentSnapshot> emailResult = emailQuery.getDocuments();
                            if(emailResult.isEmpty()) {
                                Db.getUsersCollectionReference().
                                    whereEqualTo(Db.IDNUMBER_FIELD, idNumber).
                                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot idNumberQuery = task.getResult();
                                        List<DocumentSnapshot> idNumberResult = idNumberQuery.getDocuments();
                                        if(idNumberResult.isEmpty()) {

                                            Map<String, Object> input = new HashMap<>();
                                            input.put("email",email);
                                            input.put("firstName",firstName);
                                            input.put("idNumber",idNumber);
                                            input.put("lastName",lastName);
                                            input.put("password",password);
                                            input.put("usertype",usertype);
                                            input.put("username",username);
                                            Db.
                                                getUsersCollectionReference().
                                                add(input).
                                                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "Input added succesfully");
                                                        Intent intent = new Intent(RegistrationView.this, ClasslistView.class);
                                                        intent.putExtra(EMAIL_STATE_KEY,email);
                                                        intent.putExtra(USERNAME_STATE_KEY,username);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                String error = e.getMessage();
                                                Log.d(TAG, "error is "+error);
                                            }
                                        });
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "user already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

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
                !inputPassword.getText().toString().isEmpty() &&
                !inputUsername.getText().toString().isEmpty();
    }
}