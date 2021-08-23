package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Classlist extends AppCompatActivity {

    private ArrayList<ClassModel> classModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        populate_data();
        setupRecyclerView();
    }

    void populate_data(){
//        this.classModels.add(new ClassModel("MOBDEVE", "S12"));
//        this.classModels.add(new ClassModel("CCAPDEV", "S13"));
//        this.classModels.add(new ClassModel("GERPHIS", "S69"));
//        this.classModels.add(new ClassModel("MOBDEVE", "S14"));
//        this.classModels.add(new ClassModel("CCAPDEV", "S11"));
//        this.classModels.add(new ClassModel("GERPHIS", "S61"));
//        this.classModels.add(new ClassModel("MOBDEVE", "S52"));
//        this.classModels.add(new ClassModel("CCAPDEV", "S23"));
//        this.classModels.add(new ClassModel("GERPHIS", "S49"));
    }

    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.recyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        this.adapter = new Adapter(this.classModels);
        this.recyclerView.setAdapter(this.adapter);
    }


}