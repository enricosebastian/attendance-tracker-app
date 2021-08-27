package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateCourse extends AppCompatActivity {

    // From Views
    private Button btnCancelCreate, btnCreateCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        this.btnCancelCreate = findViewById(R.id.btnCancelCreate);
        this.btnCreateCourse = findViewById(R.id.btnCreateCourse);

        //When user cancels
        this.btnCancelCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //When user edits the course
        this.btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert logic here
                Toast.makeText(CreateCourse.this, "Course name edited", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}