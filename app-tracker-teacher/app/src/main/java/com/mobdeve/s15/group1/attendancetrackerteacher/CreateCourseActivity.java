package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateCourseActivity extends AppCompatActivity {
    private static final String TAG = "CreateCourseActivity.java";

    //shared preferences initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;
    ////////////

    // From Views
    private Button btnCancelCreate, btnCreateCourse;
    private EditText etCourseName, etCourseCode, etCourseSection;
    private Switch switchIsPublished;

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
                String courseName   = etCourseName.getText().toString();
                String courseCode   = etCourseCode.getText().toString();
                String sectionCode  = etCourseSection.getText().toString(); //FOKEN INCONSISTENT NAMING CONVENTION LMAOOOOOOOOOOO
                boolean isPublished = switchIsPublished.isChecked();

                if(courseName.isEmpty() || courseCode.isEmpty() || sectionCode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Entry field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Db.getDocumentsWith(Db.COLLECTION_COURSES,
                            Db.FIELD_COURSECODE, courseCode, Db.FIELD_SECTIONCODE, sectionCode).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<DocumentSnapshot> result = Db.getDocuments(task);
                            if(result.size()==0) {
                                addClass(courseName, courseCode, sectionCode, isPublished);

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "That class already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

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