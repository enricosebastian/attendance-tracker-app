package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    private static final String TAG = "SingleClassActivity";

    //shared preferences initialization
    private SharedPreferences           sp;
    private SharedPreferences.Editor    editor;
    private String                      email;
    ////////////

    //recycler view initialization
    private ArrayList<MeetingModel>     meetingModels = new ArrayList<>();
    private RecyclerView                singleClassRecyclerView;
    private RecyclerView.LayoutManager  singleClassLayoutManager;
    private SingleClassAdapter          singleClassAdapter;

    //widget initialization
    private TextView            txtClassCodeTitle,
                                txtClassNameSubtitle,
                                txtCreateMeeting,
                                txtStudentCount;
    private ImageButton         btnAcceptStudents;
    private String              courseCode,
                                sectionCode,
                                courseName;
    private SwipeRefreshLayout  refreshLayout;

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
        this.txtStudentCount        = findViewById(R.id.txtStudentCount);
        this.txtCreateMeeting       = findViewById(R.id.txtCreateMeeting);
        this.btnAcceptStudents      = findViewById(R.id.btnAcceptStudents);
        this.refreshLayout          = findViewById(R.id.refreshLayout);


        // Clicking the create button brings the user to the create meeting activity
        txtCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleClassActivityIntent = new Intent(SingleClassActivity.this, CreateMeetingActivity.class);
                singleClassActivityIntent.putExtra(Keys.INTENT_COURSECODE, courseCode);
                singleClassActivityIntent.putExtra(Keys.INTENT_SECTIONCODE, sectionCode);
                startActivity(singleClassActivityIntent);
            }
        });

        btnAcceptStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acceptStudentsActivityIntent = new Intent(SingleClassActivity.this, AcceptStudentsActivity.class);
                acceptStudentsActivityIntent.putExtra(Keys.INTENT_COURSECODE, courseCode);
                acceptStudentsActivityIntent.putExtra(Keys.INTENT_SECTIONCODE, sectionCode);
                startActivity(acceptStudentsActivityIntent);
            }
        });
        
        txtStudentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getStudentsListActivity = new Intent(SingleClassActivity.this, ClassListActivity.class);
                getStudentsListActivity.putExtra(Keys.INTENT_COURSECODE, courseCode);
                getStudentsListActivity.putExtra(Keys.INTENT_SECTIONCODE, sectionCode);
                startActivity(getStudentsListActivity);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeRecyclerView();
                refreshLayout.setRefreshing(false);
            }
        });
    }


    protected void initializeViews() {

        txtClassCodeTitle.setText(courseCode + " - " + sectionCode);
        txtClassNameSubtitle.setText(courseName.toUpperCase());

        Db.getDocumentsWith(Db.COLLECTION_CLASSLIST,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> results = Db.getDocuments(task);
                txtStudentCount.setText(results.size()+" students");
            }
        });

        initializeRecyclerView();

    }

    protected void initializeRecyclerView() {
        Db.getDocumentsWith(Db.COLLECTION_MEETINGS,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_MEETINGSTART, Query.Direction.DESCENDING).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> results = Db.getDocuments(task);

                meetingModels.clear();
                meetingModels.addAll(Db.toMeetingModel(results));

                singleClassRecyclerView     = findViewById(R.id.SingleClassRecyclerView);
                singleClassLayoutManager    = new LinearLayoutManager(getApplicationContext());
                singleClassAdapter          = new SingleClassAdapter(meetingModels);

                singleClassRecyclerView.setLayoutManager(singleClassLayoutManager);
                singleClassRecyclerView.setAdapter(singleClassAdapter);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        initializeViews();
        Log.d(TAG, "you are on resume");
    }

}