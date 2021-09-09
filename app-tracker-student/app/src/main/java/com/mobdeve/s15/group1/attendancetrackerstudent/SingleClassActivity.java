package com.mobdeve.s15.group1.attendancetrackerstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    TextView txtClassCodeTitle, txtClassNameSubtitle;
    private String courseCode, sectionCode, courseName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        Intent getIntent = getIntent();
        this.courseCode = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.courseName = getIntent.getStringExtra(Keys.INTENT_COURSENAME);

        this.txtClassCodeTitle = findViewById(R.id.txtClassCodeTitle);
        this.txtClassNameSubtitle = findViewById(R.id.txtClassNameSubtitle);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(SingleClassActivity.this);

        String classCodeTitle = courseCode + " - " + sectionCode;
        String classNameSubtitle = courseName;

        txtClassCodeTitle.setText(classCodeTitle);
        txtClassNameSubtitle.setText(classNameSubtitle.toUpperCase());
    }

    //Initializes the views of the courses handled by the user
    protected void initializeViews() {
        //Show Progress bar
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

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
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.dismiss();
                    }
                });
    }

    protected void onResume() {
        super.onResume();
        initializeViews();
        Log.d(TAG, "you are on resume");
    }
}