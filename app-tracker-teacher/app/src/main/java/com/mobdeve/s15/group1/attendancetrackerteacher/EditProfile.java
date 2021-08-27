package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {

    EditText inputPassword, inputFirstName, inputLastname, inputEmail;
    Button btnSelectImage, btnSaveEditProfile, btnCancelEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.inputPassword = findViewById(R.id.inputPassword);
        this.inputFirstName = findViewById(R.id.inputFirstName);
        this.inputLastname = findViewById(R.id.inputLastName);
        this.inputEmail = findViewById(R.id.inputEmail);

        this.btnSelectImage = findViewById(R.id.btnSelectImage);
        this.btnSaveEditProfile = findViewById(R.id.btnSaveEditProfile);
        this.btnCancelEditProfile = findViewById(R.id.btnCancelEditProfile);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivity(intent);

                //implement picasso
            }
        });

        btnCancelEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "Changes saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}