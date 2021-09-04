package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SingleMeetingView extends AppCompatActivity {
    
    private TextView txtStatus;
    private TextView txtDate;
    private TextView txtClassTitle;

    private ArrayList<StudentPresentListModel> studentPresentListModels = new ArrayList<>();

    private RecyclerView studentListRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SingleMeetingAdapter singleMeetingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_meeting_view);

        Intent intent = getIntent();

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