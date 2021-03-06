package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.xml.transform.Result;
/*
    Allows a teacher to change the name and Publish/Unpublish of a particular Course.
 */
public class EditCourseActivity extends AppCompatActivity {
    private static final String TAG = "EditCourseActivity.java";

    // From Views
    private Button btnCancelEdit, btnSaveEdit;
    private TextView txtEditACourseHeader;
    private EditText etEditCourse;
    private Switch switchIsPublished;

    private String courseCode, sectionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent getIntent            = getIntent();
        this.courseCode             = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode            = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);

        this.btnCancelEdit          = findViewById(R.id.btnCancelEdit);
        this.btnSaveEdit            = findViewById(R.id.btnSaveEdit);
        this.txtEditACourseHeader   = findViewById(R.id.txtEditACourseHeader);
        this.etEditCourse           = findViewById(R.id.etEditCourse);
        this.switchIsPublished      = findViewById(R.id.switchIsPublished);

        txtEditACourseHeader.setText("Edit "+courseCode+" - "+sectionCode);

        initializeViews();

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
                String courseName = etEditCourse.getText().toString();
                if(courseName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Entry field is empty, please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    updateCourseName(courseName);

                }
            }
        });
    }

    //Updates the course
    protected void updateCourseName(String courseName) {
        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String documentId = Db.getIdFromTask(task);

                Db.getCollection(Db.COLLECTION_COURSES).
                document(documentId).
                update(Db.FIELD_COURSENAME, courseName, Db.FIELD_ISPUBLISHED, switchIsPublished.isChecked()).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG,"Successfully edited course");
                        Toast.makeText(EditCourseActivity.this, "Course successfully edited", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
    }

    //initializes views of activity
    protected void initializeViews() {
        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                etEditCourse.setText(result.get(0).getString(Db.FIELD_COURSENAME));
                switchIsPublished.setChecked(result.get(0).getBoolean(Db.FIELD_ISPUBLISHED));
            }
        });
    }

}