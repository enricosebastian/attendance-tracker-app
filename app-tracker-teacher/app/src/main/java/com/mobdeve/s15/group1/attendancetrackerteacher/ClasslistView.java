package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ClasslistView extends AppCompatActivity {

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
        this.classModels = new ClassDataHelper().initializeData();
    }

    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.recyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        this.adapter = new Adapter(this.classModels);
        this.recyclerView.setAdapter(this.adapter);
    }


}