package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;
    ////////////

    //recycler view initialization
    private ArrayList<MeetingModel> meetingModels = new ArrayList<>();
    private RecyclerView singleClassRecyclerView;
    private RecyclerView.LayoutManager singleClassLayoutManager;
    private SingleClassAdapter singleClassAdapter;

    //widget initialization
    TextView txtClassTitle, txtCreateMeeting, txtStudentCount;
    ImageButton btnAcceptStudents;
    private String courseCode, sectionCode, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        Intent getIntent        = getIntent();
        this.courseCode         = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode        = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.courseName         = getIntent.getStringExtra(Keys.INTENT_COURSENAME);

        this.txtClassTitle      = findViewById(R.id.txtClassTitle);
        this.txtStudentCount    = findViewById(R.id.txtStudentCount);
        this.txtCreateMeeting   = findViewById(R.id.txtCreateMeeting);
        this.btnAcceptStudents  = findViewById(R.id.btnAcceptStudents);


        txtClassTitle.setText(courseCode+" "+sectionCode+" | \""+courseName+"\"");


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
    }


    protected void initializeViews() {
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
                singleClassRecyclerView = findViewById(R.id.SingleClassRecyclerView);
                singleClassLayoutManager = new LinearLayoutManager(getApplicationContext());
                singleClassRecyclerView.setLayoutManager(singleClassLayoutManager);
                singleClassAdapter = new SingleClassAdapter(meetingModels);
                singleClassRecyclerView.setAdapter(singleClassAdapter);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        initializeViews();
        Log.d(TAG, "you are on resume");
    }


    protected void onStart() {
        super.onStart();
        initializeViews();
    }
}