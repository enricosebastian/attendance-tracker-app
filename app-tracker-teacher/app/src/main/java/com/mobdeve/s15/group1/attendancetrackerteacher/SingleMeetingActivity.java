package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
    Displays the list of students who successfully attended the class with the code displayed
    in the middle of the view. A class can be set to Open or Closed to allow/prevent students
    from attending. Finally, there is an option at the bottom to delete the meeting.

 */
public class SingleMeetingActivity extends AppCompatActivity {

    private static final String         TAG = "SingleMeetingActivity";

    //shared preferences initialization
    private SharedPreferences           sp;
    private SharedPreferences.Editor    editor;
    private String                      email;

    //recycler view initialization
    private RecyclerView                        studentListRecyclerView;
    private RecyclerView.LayoutManager          studentListLayoutManager;
    private SingleMeetingAdapter                singleMeetingAdapter;
    private ArrayList<StudentPresentListModel>  studentPresentListModels = new ArrayList<>();

    //widget initialization
    private TextView            txtStatus,
                                txtDate,
                                txtClassTitle,
                                txtMeetingCode,
                                txtClassNameSubtitle;
    private Button              btnDelete;
    private SwipeRefreshLayout  refreshLayout;

    private String              courseCode,
                                sectionCode,
                                meetingCode,
                                courseName;
    private boolean             isOpen; //suggestion: what happens if you change boolean (primitive) to Boolean instead?
    private Date                meetingStart;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_meeting_view);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor         = sp.edit();
        this.email          = sp.getString(Keys.SP_EMAIL_KEY, "");

        Intent getIntent    = getIntent();
        this.courseCode     = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode    = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.meetingCode    = getIntent.getStringExtra(Keys.INTENT_MEETINGCODE);
        this.isOpen         = getIntent.getBooleanExtra(Keys.INTENT_ISOPEN, false);
        String stringDate   = getIntent.getStringExtra(Keys.INTENT_MEETINGSTART);

        Log.d(TAG, meetingCode);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(SingleMeetingActivity.this);

        try {
            meetingStart = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.txtStatus                  = findViewById(R.id.txtStatus);
        this.txtDate                    = findViewById(R.id.txtDate);
        this.txtClassTitle              = findViewById(R.id.txtClassCodeTitle);
        this.txtClassNameSubtitle       = findViewById(R.id.txtClassNameSubtitle);
        this.txtMeetingCode             = findViewById(R.id.tvMeetingCode);
        this.studentListRecyclerView    = findViewById(R.id.studentListRecyclerView);
        this.btnDelete                  = findViewById(R.id.btnDelete);
        this.refreshLayout              = findViewById(R.id.refreshLayout);

        //When user refreshes the layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeRecyclerView();
                refreshLayout.setRefreshing(false);
            }
        });

        //When the user wants to delete the meeting
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMeeting(Db.COLLECTION_MEETINGS, Db.FIELD_MEETINGCODE, meetingCode);
            }
        });

        // If the status was clicked by the user
        txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtStatus.getText().toString().equals("CLOSED")) {
                    txtStatus.setText("OPEN");
                    txtStatus.setBackgroundTintList(v.getContext().getResources().getColorStateList(R.color.light_green));

                    Db.getDocumentsWith(Db.COLLECTION_MEETINGS,
                    Db.FIELD_MEETINGCODE, meetingCode).
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String documentId = Db.getIdFromTask(task);

                            Db.getCollection(Db.COLLECTION_MEETINGS).document(documentId).
                            update(Db.FIELD_ISOPEN, true).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Successfully updated db");
                                }
                            });
                        }
                    });
                } else if(txtStatus.getText().toString().equals("OPEN")) {
                    txtStatus.setText("CLOSED");
                    txtStatus.setBackgroundTintList(v.getContext().getResources().getColorStateList(R.color.red_light));

                    Db.getDocumentsWith(Db.COLLECTION_MEETINGS,
                    Db.FIELD_MEETINGCODE, meetingCode).
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String documentId = Db.getIdFromTask(task);

                            Db.getCollection(Db.COLLECTION_MEETINGS).document(documentId).
                            update(Db.FIELD_ISOPEN, false).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Successfully updated db");
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    // This method initializes the view of the activity
    protected void initializeViews() {
        //Show Progress bar
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy | E");
        txtDate.setText(dateFormat.format(meetingStart));

        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_COURSECODE, courseCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                courseName = result.get(0).getString(Db.FIELD_COURSENAME);
                String classCodeTitle = courseCode + " - " + sectionCode;
                String classNameSubtitle = courseName;
                txtClassTitle.setText(classCodeTitle);
                txtClassNameSubtitle.setText(classNameSubtitle);
                txtMeetingCode.setText(meetingCode);

                // Checks the status adjusts the background based on the current status
                if(isOpen) {
                    txtStatus.setText("OPEN");
                    txtStatus.setBackgroundTintList(getResources().getColorStateList(R.color.light_green));
                } else {
                    txtStatus.setText("CLOSED");
                    txtStatus.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                }
                initializeRecyclerView();
            }
        });
    }

    // Initializes the recycler view of the single meeting activity
    protected void initializeRecyclerView() {
        Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
        Db.FIELD_MEETINGCODE, meetingCode, Db.FIELD_LASTNAME, Query.Direction.ASCENDING).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                studentPresentListModels = Db.toStudentPresentListModel(result);
                studentListLayoutManager = new LinearLayoutManager(getApplicationContext());
                studentListRecyclerView.setLayoutManager(studentListLayoutManager);
                singleMeetingAdapter = new SingleMeetingAdapter(studentPresentListModels);
                studentListRecyclerView.setAdapter(singleMeetingAdapter);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.dismiss();
            }
        });
    }

    // When user deletes a meeting
    public void deleteMeeting(String tableName, String field, String value) {

        Db.deleteDocuments(Db.COLLECTION_MEETINGHISTORY, field, value);

        Db.getDocumentsWith(tableName, field, value).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = Db.getIdFromTask(task);
                Db.getCollection(tableName).
                document(id).
                delete().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"Successfully deleted a "+tableName+" document");

                        } else {
                            Log.d(TAG,"Failed: "+task.getException());
                        }
                        finish();
                    }
                });
            }
        });
    }

    //Initialize views on resume
    protected void onResume() {
        super.onResume();
        initializeViews();
    }
}