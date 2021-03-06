package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
    This activity is launched when a teacher wants to create a new Course. The fields required
    are, 1.) Course Name, 2.) Course Code, and 3.) Section. The teacher also has an option
    to determine if the new course should be published right away or not.
 */
public class CreateCourseActivity extends AppCompatActivity {
    private static final String TAG = "CreateCourseActivity.java";

    //shared preferences initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;

    //widget initialization
    private Button btnCancelCreate, btnCreateCourse;
    private EditText etCourseName, etCourseCode, etCourseSection;
    private Switch switchIsPublished;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor         = sp.edit();
        this.email          = sp.getString(Keys.SP_EMAIL_KEY,"");

        this.btnCancelCreate    = findViewById(R.id.btnCancelCreate);
        this.btnCreateCourse    = findViewById(R.id.btnCreateCourse);
        this.etCourseCode       = findViewById(R.id.etCourseCode);
        this.etCourseName       = findViewById(R.id.etCourseName);
        this.etCourseSection    = findViewById(R.id.etCourseSection);
        this.switchIsPublished  = findViewById(R.id.switchIsPublished);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(CreateCourseActivity.this);

        //When user cancels
        this.btnCancelCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //When user creates the course
        this.btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert logic here
                String courseName   = etCourseName.getText().toString().trim();
                String courseCode   = etCourseCode.getText().toString().trim();
                String sectionCode  = etCourseSection.getText().toString().trim();
                boolean isPublished = switchIsPublished.isChecked();

                progressDialog.setMessage("Loading...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                if(courseName.isEmpty() || courseCode.isEmpty() || sectionCode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Entry field is empty", Toast.LENGTH_SHORT).show();
                    //Dismiss the loading dialog once everything is finished
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.dismiss();
                } else {
                    Db.getDocumentsWith(Db.COLLECTION_COURSES,
                            Db.FIELD_COURSECODE, courseCode, Db.FIELD_SECTIONCODE, sectionCode).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<DocumentSnapshot> result = Db.getDocuments(task);

                                // If course already exists
                                if(result.size()==0) {
                                    addClass(courseName, courseCode, sectionCode, isPublished);
                                    //Dismiss the loading dialog once everything is finished
                                    progressDialog.setCanceledOnTouchOutside(true);
                                    progressDialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "That course already exists!", Toast.LENGTH_SHORT).show();
                                    //Dismiss the loading dialog once everything is finished
                                    progressDialog.setCanceledOnTouchOutside(true);
                                    progressDialog.dismiss();
                                }
                            }
                    });

                }
            }
        });

    }

    // Creates a new Course
    protected void addClass(String courseName, String courseCode, String sectionCode, boolean isPublished) {
        Map<String, Object> input = new HashMap<>();

        input.put(Db.FIELD_COURSECODE, courseCode);
        input.put(Db.FIELD_COURSENAME, courseName);
        input.put(Db.FIELD_HANDLEDBY, email);
        input.put(Db.FIELD_ISPUBLISHED, isPublished);
        input.put(Db.FIELD_SECTIONCODE, sectionCode);
        input.put(Db.FIELD_STUDENTCOUNT, 0);

        Db.addDocument(Db.COLLECTION_COURSES, input);

        Toast.makeText(CreateCourseActivity.this, "Course successfully created", Toast.LENGTH_SHORT).show();
    }
}