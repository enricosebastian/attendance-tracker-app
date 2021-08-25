package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterFormActivity extends AppCompatActivity {

    int IDnumber;
    EditText etFirstName, etLastName, eteEmail, etpPassword, etpReenterPassword;
    ImageButton btnSubmitRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        this.etFirstName = findViewById(R.id.etFirstName);
        this.etLastName = findViewById(R.id.etLastName);
        this.eteEmail = findViewById(R.id.eteEmail);
        this.etpPassword = findViewById(R.id.etpPassword);
        this.etpReenterPassword = findViewById(R.id.etpReenterPassword);
        this.btnSubmitRegister = findViewById(R.id.btnSubmitRegister);

        Intent intent = getIntent();
        this.IDnumber = intent.getIntExtra("NEW_ID_KEY", 9999);

        this.btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if all the fields not empty
                if(!etFirstName.getText().toString().isEmpty())
                {

                    Intent intent = new Intent();
                    String firstName, lastName, email, password;
                    firstName = etFirstName.getText().toString();
                    lastName = etLastName.getText().toString();
                    email = eteEmail.getText().toString();
                    password = etpPassword.getText().toString();

                    intent.putExtra(MyKeys.ID_NUMBER_KEY.name(), IDnumber);
                    intent.putExtra(MyKeys.FIRST_NAME_KEY.name(), firstName);
                    intent.putExtra(MyKeys.LAST_NAME_KEY.name(), lastName);
                    intent.putExtra(MyKeys.EMAIL_KEY.name(), email);
                    intent.putExtra(MyKeys.PASSWORD_KEY.name(), password);
                    // get all info and send it back to the previous activity for db processing

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else {
                    Toast.makeText(RegisterFormActivity.this,
                            "Error: field error <demo>", Toast.LENGTH_SHORT).
                            show();
                }
            }
        });
    }
    public void checkFields()
    {
        /*
            TODO:
                1. Validate each field one by one.
                2. Add to the StringBuilder the error message (so it can be displayed on the Toast)
         */
    }

}