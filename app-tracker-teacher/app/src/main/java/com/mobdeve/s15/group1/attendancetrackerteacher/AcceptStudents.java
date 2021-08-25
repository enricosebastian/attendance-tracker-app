package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class AcceptStudents extends AppCompatActivity {

    private ArrayList<Student> students = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AcceptStudentsAdapter acceptStudentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_students);
    }

//    void setupRecyclerView () {
//        this.recyclerView = findViewById(R.id.recyclerView);
//
//        this.layoutManager = new LinearLayoutManager(this);
//        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        this.acceptStudentsAdapter = new AcceptStudentsAdapter(this.students);
//        this.recyclerView.setAdapter(this.acceptStudentsAdapter);
//    }
}