package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
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
/*
    On a particular Course and Section, the teacher can see the list of students
    who wishes to join with an option to accept or reject.
 */
public class AcceptStudentsActivity extends AppCompatActivity {

    private static final String TAG = "SingleClassActivity";

    private ArrayList<CourseRequestModel>   courseRequestModels = new ArrayList<>();
    private RecyclerView                    acceptStudentsRecyclerView;
    private RecyclerView.LayoutManager      acceptStudentsLayoutManager;
    private AcceptStudentsAdapter           acceptStudentsAdapter;

    private ProgressDialog      progressDialog;
    private SwipeRefreshLayout  refreshLayout;
    private String              courseCode,
                                sectionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_students);

        Intent getIntent    = getIntent();
        this.sectionCode    = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.courseCode     = getIntent.getStringExtra(Keys.INTENT_COURSECODE);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(AcceptStudentsActivity.this);
        this.refreshLayout  = findViewById(R.id.refreshLayout);

        // When user swipes down to refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeViews(); //can also be initializeViews() if you want, for that loading screen
                refreshLayout.setRefreshing(false);
            }
        });

    }

    // Initializes view which is called when the view is refreshed on resume
    protected void initializeViews() {
        //Show Progress bar
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);
        initializeRecyclerView();
    }

    // Initializes recycler view
    protected void initializeRecyclerView() {
        //Gets the data of students who want to join the course
        Db.getDocumentsWith(Db.COLLECTION_COURSEREQUEST,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                courseRequestModels.clear(); //always clear when initializing

                List<DocumentSnapshot> result = Db.getDocuments(task);
                courseRequestModels.addAll(Db.toCourseRequestModel(result));

                acceptStudentsRecyclerView = findViewById(R.id.acceptStudentsRecyclerView);
                acceptStudentsLayoutManager = new LinearLayoutManager(getApplicationContext());
                acceptStudentsRecyclerView.setLayoutManager(acceptStudentsLayoutManager);
                acceptStudentsAdapter = new AcceptStudentsAdapter(courseRequestModels);
                acceptStudentsRecyclerView.setAdapter(acceptStudentsAdapter);

                //Should be displayed after setting up recycler view
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.dismiss();
            }
        });
    }

    // Initializes view on resume
    protected void onResume() {
        super.onResume();
        initializeViews();
    }
}