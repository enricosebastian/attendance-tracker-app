package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AcceptStudentsActivity extends AppCompatActivity {

    private static final String TAG = "SingleClassActivity";

    private ArrayList<CourseRequestModel> courseRequestModels = new ArrayList<>();
    private RecyclerView acceptStudentsRecyclerView;
    private RecyclerView.LayoutManager acceptStudentsLayoutManager;
    private AcceptStudentsAdapter acceptStudentsAdapter;

    private String courseCode, sectionCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_students);

        Intent getIntent = getIntent();
        this.sectionCode = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.courseCode = getIntent.getStringExtra(Keys.INTENT_COURSECODE);

        initializeViews();
    }

    protected void initializeViews() {
        Db.getDocumentsWith(Db.COLLECTION_COURSEREQUEST,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                courseRequestModels.addAll(Db.toCourseRequestModel(result));

                acceptStudentsRecyclerView = findViewById(R.id.acceptStudentsRecyclerView);
                acceptStudentsLayoutManager = new LinearLayoutManager(getApplicationContext());
                acceptStudentsRecyclerView.setLayoutManager(acceptStudentsLayoutManager);
                acceptStudentsAdapter = new AcceptStudentsAdapter(courseRequestModels);
                acceptStudentsRecyclerView.setAdapter(acceptStudentsAdapter);
            }
        });
    }
}