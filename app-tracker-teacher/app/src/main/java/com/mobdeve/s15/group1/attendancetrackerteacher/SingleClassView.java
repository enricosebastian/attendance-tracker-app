package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SingleClassView extends AppCompatActivity {

    private ArrayList<MeetingModel> meetingModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SingleClassAdapter adapter;

    TextView txtAddClass, txtClassTitle;
    private String _id, classCode, sectionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        Intent intent = getIntent();
        this._id = intent.getStringExtra("_ID");
        this.classCode = intent.getStringExtra("CLASSCODE");
        this.sectionCode = intent.getStringExtra("SECTIONCODE");

        this.txtClassTitle = findViewById(R.id.txtClassTitle);
        txtClassTitle.setText(classCode+" - "+sectionCode);

        this.txtAddClass = findViewById(R.id.txtAddClass);
        txtAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
            }
        });

        this.meetingModels = new MeetingDataHelper().initializeData();
        setupRecyclerView();

    }

    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.SingleClassRecyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.adapter = new SingleClassAdapter(this.meetingModels);
        this.recyclerView.setAdapter(this.adapter);
    }
}