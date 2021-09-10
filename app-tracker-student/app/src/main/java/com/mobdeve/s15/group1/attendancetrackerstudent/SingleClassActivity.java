package com.mobdeve.s15.group1.attendancetrackerstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SingleClassActivity extends AppCompatActivity {

    private static final String         TAG = "SingleClassActivity";


    //recycler view initialization
    private ArrayList<MeetingModel>     meetingModels = new ArrayList<>();
    private RecyclerView                singleClassRecyclerView;
    private RecyclerView.LayoutManager  singleClassLayoutManager;
    private SingleClassAdapter          singleClassAdapter;

    //widget initialization
    private ProgressDialog              progressDialog;
    private TextView                    txtClassCodeTitle,
                                        txtClassNameSubtitle;
    private String                      courseCode,
                                        sectionCode,
                                        courseName;
    private SwipeRefreshLayout          refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        Intent getIntent            = getIntent();
        this.courseCode             = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode            = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.courseName             = getIntent.getStringExtra(Keys.INTENT_COURSENAME);

        this.txtClassCodeTitle      = findViewById(R.id.txtClassCodeTitle);
        this.txtClassNameSubtitle   = findViewById(R.id.txtClassNameSubtitle);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog         = new ProgressDialog(SingleClassActivity.this);
        this.refreshLayout          = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeRecyclerView();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    //Initializes the views of the courses handled by the user
    protected void initializeViews() {
        //loading screen for dramatic effect
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

        String classCodeTitle       = courseCode + " - " + sectionCode;
        String classNameSubtitle    = courseName;

        //sets up course code header and class subtitle bar
        txtClassCodeTitle.setText(classCodeTitle);
        txtClassNameSubtitle.setText(classNameSubtitle.toUpperCase());

        initializeRecyclerView();
    }

    //gathers all the existing data wherein the student is part of the classlist
    protected void initializeRecyclerView() {
        Db.getDocumentsWith(Db.COLLECTION_MEETINGS,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_MEETINGSTART, Query.Direction.DESCENDING).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                meetingModels.clear(); //ALWAYS clear before adding models, to prevent duplication

                List<DocumentSnapshot> results = Db.getDocuments(task);
                meetingModels.addAll(Db.toMeetingModel(results));

                singleClassRecyclerView     = findViewById(R.id.SingleClassRecyclerView);
                singleClassLayoutManager    = new LinearLayoutManager(getApplicationContext());
                singleClassAdapter          = new SingleClassAdapter(meetingModels);

                singleClassRecyclerView.setLayoutManager(singleClassLayoutManager);
                singleClassRecyclerView.setAdapter(singleClassAdapter);

                //removes loading screen after finishing
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.dismiss();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        initializeViews();
    }
}