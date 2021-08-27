package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EditCourse extends AppCompatActivity {

    // From Views
    private Button btnCancelEdit, btnSaveEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        this.btnCancelEdit = findViewById(R.id.btnCancelEdit);
        this.btnSaveEdit = findViewById(R.id.btnSaveEdit);

        //When user cancels
        this.btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //When user creates the course
        this.btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert logic here
                Toast.makeText(EditCourse.this, "Course successfully edited", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}