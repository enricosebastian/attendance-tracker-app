package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SingleMeetingActivity extends AppCompatActivity {

    private static final String TAG = "SingleMeetingActivity.java";

    //shared preferences initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;
    ////////////

    //recycler view initialization
    private RecyclerView studentListRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SingleMeetingAdapter singleMeetingAdapter;
    private ArrayList<StudentPresentListModel> studentPresentListModels = new ArrayList<>();
    ////////////////////

    //widget initialization
    private TextView txtStatus;
    private TextView txtDate;
    private TextView txtClassTitle;
    ////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_meeting_view);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor         = sp.edit();
        this.email          = sp.getString(Keys.SP_EMAIL_KEY,"");

        Intent intent = getIntent(); //delete lmao

        this.txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra(MyKeys.DATE_KEY.name()));

        String courseCode = intent.getStringExtra(MyKeys.COURSE_CODE_KEY.name());
        String sectionCode = intent.getStringExtra(MyKeys.SECTION_CODE_KEY.name());
        String meetingCode = intent.getStringExtra(MyKeys.MEETING_CODE_KEY.name());

        this.txtClassTitle = findViewById(R.id.txtClassTitle);
        txtClassTitle.setText(courseCode +" - "+sectionCode);
        
        this.txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtStatus.getText().toString().equals("CLOSED")) {
                    txtStatus.setText("OPEN");
                    txtStatus.setBackgroundTintList(v.getContext().getResources().getColorStateList(R.color.light_green));
                } else if(txtStatus.getText().toString().equals("OPEN")) {
                    txtStatus.setText("CLOSED");
                    txtStatus.setBackgroundTintList(v.getContext().getResources().getColorStateList(R.color.red_light));
                }
            }
        });

        Task<QuerySnapshot> querySnapshotTask = Db.getStudentsInMeeting(meetingCode);
        querySnapshotTask.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.toList(task);
                ArrayList<StudentPresentListModel> studentPresentListModels = Db.toStudentPresentListModel(result);

                studentListRecyclerView = findViewById(R.id.studentListRecyclerView);

                layoutManager = new LinearLayoutManager(getApplicationContext());
                studentListRecyclerView.setLayoutManager(layoutManager);

                singleMeetingAdapter = new SingleMeetingAdapter(studentPresentListModels);
                studentListRecyclerView.setAdapter(singleMeetingAdapter);
            }
        });

    }
}