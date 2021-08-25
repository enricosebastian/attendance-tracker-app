package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterTeacherActivity extends AppCompatActivity {
    private EditText etNewID;
    private Button btnNext;

    private ActivityResultLauncher<Intent> newUserResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        /*
                            TODO:
                                1. Get all the fields
                                2. Create an Account object (of a teacher) for DB insertion
                                3. Insert to Firebase
                        */
                        String firstName, lastName, email, password;
                        int IDnumber;
                        IDnumber = result.getData().getIntExtra(MyKeys.ID_NUMBER_KEY.name(), 9999);
                        firstName = result.getData().getStringExtra(MyKeys.FIRST_NAME_KEY.name());
                        lastName = result.getData().getStringExtra(MyKeys.LAST_NAME_KEY.name());
                        email = result.getData().getStringExtra(MyKeys.EMAIL_KEY.name());
                        password = result.getData().getStringExtra(MyKeys.PASSWORD_KEY.name());

                        Toast.makeText(RegisterTeacherActivity.this,
                                "[DEBUG]: " + IDnumber + ", " + firstName + ", " + lastName + ", " + email + ", " + password, Toast.LENGTH_SHORT).
                                show();
                        // get all the fields and add it to the database?
                        finish();
                    }
                }
            }
    );
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        this.etNewID = findViewById(R.id.etNewID);
        this.btnNext = findViewById(R.id.btnNext);

        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the text from the field then launch activity


                // if username is not taken in the DB, continue to register. else, send a toast

                Intent intent = new Intent(RegisterTeacherActivity.this, RegisterFormActivity.class);
                intent.putExtra("NEW_ID_KEY", Integer.parseInt(etNewID.getText().toString()));

                newUserResultLauncher.launch(intent);

            }
        });
    }
}