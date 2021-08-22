package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SingleClass extends AppCompatActivity {

    private ArrayList<Meeting> meetings = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SingleClassAdapter adapter;
    
    TextView txtAddClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        this.txtAddClass = findViewById(R.id.txtAddClass);
        
        txtAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
            }
        });

        populate_data();
        setupRecyclerView();

    }

    void populate_data(){
        this.meetings.add(new Meeting("January", 21,1969, "M", 69));
    }

    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.SingleClassRecyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.adapter = new SingleClassAdapter(this.meetings);
        this.recyclerView.setAdapter(this.adapter);
    }
}