package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Classlist extends AppCompatActivity {

    private ArrayList<Class> classes = new ArrayList<>();

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
        this.classes.add(new Class("MOBDEVE", "S12"));
        this.classes.add(new Class("CCAPDEV", "S13"));
        this.classes.add(new Class("GERPHIS", "S69"));
        this.classes.add(new Class("MOBDEVE", "S14"));
        this.classes.add(new Class("CCAPDEV", "S11"));
        this.classes.add(new Class("GERPHIS", "S61"));
        this.classes.add(new Class("MOBDEVE", "S52"));
        this.classes.add(new Class("CCAPDEV", "S23"));
        this.classes.add(new Class("GERPHIS", "S49"));
    }

    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.recyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        this.adapter = new Adapter(this.classes);
        this.recyclerView.setAdapter(this.adapter);
    }


}