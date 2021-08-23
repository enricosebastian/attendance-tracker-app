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
    private ClasslistAdapter classlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.classModels = new ClassDataHelper().initializeData();
        setupRecyclerView();
    }

    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.recyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        this.classlistAdapter = new ClasslistAdapter(this.classModels);
        this.recyclerView.setAdapter(this.classlistAdapter);
    }

}