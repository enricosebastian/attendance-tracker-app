package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SingleClassView extends AppCompatActivity {

    private ArrayList<MeetingModel> meetingModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SingleClassAdapter adapter;

    TextView txtAddClass, txtClassTitle;
    ImageButton btnAcceptStudents;
    private String classCode, sectionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        Intent intent = getIntent();
        this.classCode = intent.getStringExtra(MyKeys.COURSE_CODE_KEY.name());
        this.sectionCode = intent.getStringExtra(MyKeys.SECTION_CODE_KEY.name());

        this.txtClassTitle = findViewById(R.id.txtClassTitle);
        this.btnAcceptStudents = findViewById(R.id.btnAcceptStudents);

        txtClassTitle.setText(classCode+" - "+sectionCode);

        Db.getMeetingsCollectionReference().
                whereEqualTo(Db.COURSECODE_FIELD, classCode)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> result = querySnapshot.getDocuments();

                        for(DocumentSnapshot ds:result) {
                            //MeetingModel(String courseCode, String sectionCode, String meetingCode, Date date, int studentsPresent)
                            if(ds.get("sectionCode").toString().equals(sectionCode)) {
                                meetingModels.add(new MeetingModel(
                                        ds.get("courseCode").toString(),
                                        ds.get("sectionCode").toString(),
                                        ds.get("meetingCode").toString(),
                                        ds.getTimestamp("meetingStart").toDate(), //how to convert timestamp to Date?
                                        Integer.parseInt(ds.get("studentCount").toString())));
                            }
                        }
                        recyclerView = findViewById(R.id.SingleClassRecyclerView);

                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        adapter = new SingleClassAdapter(meetingModels);
                        recyclerView.setAdapter(adapter);
                    }
                });

        this.txtAddClass = findViewById(R.id.txtCreateMeeting);

        // Clicking the create button brings the user to the create meeting activity
        txtAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
                //insert create meeting here
                Intent intent = new Intent(SingleClassView.this, CreateMeetingView.class);
                intent.putExtra(MyKeys.COURSE_CODE_KEY.name(), classCode);
                intent.putExtra(MyKeys.SECTION_CODE_KEY.name(), sectionCode);
                startActivity(intent);
            }
        });

        btnAcceptStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleClassView.this, AcceptStudents.class);
                startActivity(intent);
            }
        });

    }

//    void setupRecyclerView() {
//        this.recyclerView = findViewById(R.id.SingleClassRecyclerView);
//
//        this.layoutManager = new LinearLayoutManager(this);
//        this.recyclerView.setLayoutManager(this.layoutManager);
//
//        this.adapter = new SingleClassAdapter(this.meetingModels);
//        this.recyclerView.setAdapter(this.adapter);
//    }
}