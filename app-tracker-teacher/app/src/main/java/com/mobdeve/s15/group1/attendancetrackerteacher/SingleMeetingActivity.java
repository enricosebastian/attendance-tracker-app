package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleMeetingActivity extends AppCompatActivity {

    private static final String TAG = "SingleMeetingActivity";

    //shared preferences initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;
    ////////////

    //recycler view initialization
    private RecyclerView studentListRecyclerView;
    private RecyclerView.LayoutManager studentListLayoutManager;
    private SingleMeetingAdapter singleMeetingAdapter;
    private ArrayList<StudentPresentListModel> studentPresentListModels = new ArrayList<>();
    ////////////////////

    //widget initialization
    private TextView txtStatus;
    private TextView txtDate;
    private TextView txtClassTitle;
    private TextView txtMeetingCode;
    private Button btnDelete;
    ////////////

    private String courseCode, sectionCode, meetingCode, courseName;
    private boolean isOpen;
    private Date meetingStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_meeting_view);

        this.sp = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();
        this.email = sp.getString(Keys.SP_EMAIL_KEY, "");

        Intent getIntent = getIntent();
        this.courseCode = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.meetingCode = getIntent.getStringExtra(Keys.INTENT_MEETINGCODE);
        this.isOpen = getIntent.getBooleanExtra(Keys.INTENT_ISOPEN, false);
        String stringDate = getIntent.getStringExtra(Keys.INTENT_MEETINGSTART);

        Log.d(TAG, meetingCode);

        try {
            meetingStart = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.txtStatus = findViewById(R.id.txtStatus);
        this.txtDate = findViewById(R.id.txtDate);
        this.txtClassTitle = findViewById(R.id.txtClassTitle);
        this.txtMeetingCode = findViewById(R.id.tvMeetingCode); //LMAO THIS NAMING INCONSISTENCY SMH WHAT A DEV JESUS FOKEN CHRIST
        this.studentListRecyclerView = findViewById(R.id.studentListRecyclerView);
        this.btnDelete = findViewById(R.id.btnDelete);

        initializeViews(); //move to on resume one day

    }

    protected void initializeViews() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy | E");
        txtDate.setText(dateFormat.format(meetingStart));

        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_COURSECODE, courseCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                courseName = result.get(0).getString(Db.FIELD_COURSENAME);
                txtClassTitle.setText(courseCode+" "+sectionCode+" | \""+courseName+"\"");
            }
        });
        txtMeetingCode.setText(meetingCode);

        if(isOpen) {
            txtStatus.setText("OPEN");
            txtStatus.setBackgroundTintList(this.getResources().getColorStateList(R.color.light_green));
        } else {
            txtStatus.setText("CLOSED");
            txtStatus.setBackgroundTintList(this.getResources().getColorStateList(R.color.red_light));
        }

        Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
        Db.FIELD_MEETINGCODE, meetingCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                studentPresentListModels = Db.toStudentPresentListModel(result);

                studentListLayoutManager = new LinearLayoutManager(getApplicationContext());
                studentListRecyclerView.setLayoutManager(studentListLayoutManager);
                singleMeetingAdapter = new SingleMeetingAdapter(studentPresentListModels);
                studentListRecyclerView.setAdapter(singleMeetingAdapter);
            }
        });

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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Db.deleteDocument(Db.COLLECTION_MEETINGS, Db.FIELD_MEETINGCODE, meetingCode);
                finish();
            }
        });

    }
}