package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClassListActivity extends AppCompatActivity {

    private static final String         TAG = "StudentsListActivity.java";

    //shared preferences initialization
    private SharedPreferences           sp;
    private SharedPreferences.Editor    editor;
    private String                      email;
    ////////////

    //recycler view initialization
    private ArrayList<ClassListModel>   classListModels = new ArrayList<>();
    private RecyclerView                classListRecyclerView;
    private RecyclerView.LayoutManager  classListLayoutManager;
    private ClassListAdapter            classListAdapter;

    //widget initialization
    private String  courseCode,
                    sectionCode;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        Intent getIntent = getIntent();
        this.sectionCode = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.courseCode = getIntent.getStringExtra(Keys.INTENT_COURSECODE);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(ClassListActivity.this);

        initializeViews();
    }

    protected void initializeViews() {
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

        Db.getDocumentsWith(Db.COLLECTION_CLASSLIST,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "inside on complete classlist");
                List<DocumentSnapshot> result = Db.getDocuments(task);
                classListModels.addAll(Db.toClassListModel(result));

                classListRecyclerView = findViewById(R.id.classListRecyclerView);
                classListLayoutManager = new LinearLayoutManager(getApplicationContext());
                classListRecyclerView.setLayoutManager(classListLayoutManager);
                classListAdapter = new ClassListAdapter(classListModels);
                classListRecyclerView.setAdapter(classListAdapter);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.dismiss();
            }
        });
    }
}