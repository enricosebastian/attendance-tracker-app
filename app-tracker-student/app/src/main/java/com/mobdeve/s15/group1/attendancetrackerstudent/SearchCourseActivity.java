package com.mobdeve.s15.group1.attendancetrackerstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchCourseActivity extends AppCompatActivity {

    private Button btnSearch, btnCancelSearch;
    private EditText etSearchCourseCode, etSearchCourseSec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);

        this.etSearchCourseCode = findViewById(R.id.etSearchCourseCode);
        this.etSearchCourseSec = findViewById(R.id.etSearchCourseSec);

        this.btnCancelSearch = findViewById(R.id.btnCancelSearch);
        this.btnSearch = findViewById(R.id.btnSearch);

        btnCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}