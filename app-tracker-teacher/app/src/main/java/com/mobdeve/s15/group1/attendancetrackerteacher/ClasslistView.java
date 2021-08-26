package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ClasslistView extends AppCompatActivity {

    private ArrayList<ClassModel> classModels = new ArrayList<>();

    private static String SP_FILE_NAME = "LoginPreferences";
    private static String USERNAME_STATE_KEY = "USERNAME_KEY";
    private static String EMAIL_STATE_KEY = "EMAIL_KEY";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClasslistAdapter classlistAdapter;
    private DocumentSnapshot donny;

    private TextView txtName;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.db = FirebaseFirestore.getInstance();

        Intent passedIntent = getIntent();
        String username = passedIntent.getStringExtra(USERNAME_STATE_KEY);
        String email = passedIntent.getStringExtra(EMAIL_STATE_KEY);

        this.txtName = findViewById(R.id.txtName);

        FirestoreReferences.getUsersCollectionReference().
                whereEqualTo(FirestoreReferences.EMAIL_FIELD, email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> result = querySnapshot.getDocuments();
                        String fullName =
                                result.get(0).get(FirestoreReferences.FIRSTNAME_FIELD).toString() + " " +
                                result.get(0).get(FirestoreReferences.FIRSTNAME_FIELD).toString();
                        txtName.setText(fullName);
                    }
                });

        //other method:
//        Task<QuerySnapshot> q2 = FirestoreReferences.getUserQuery("ben@dlsu.edu.ph");
//        q2.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                List<DocumentSnapshot> result = task.getResult().getDocuments();
//
//            }
//        });

//        debugging database helper
        //DocumentSnapshot ds = FirestoreReferences.getSingleUserData("ben@dlsu.edu.ph");

//        Log.d("in classlist view", "test:"+ds);
//        String username1 = ds.get("username").toString();
//        if(username1 == null) {
//            Log.d("classlist view","hatdog");
//        } else {
//            Log.d("classlist view else","not null");
//        }
//

        FirestoreReferences.getCoursesCollectionReference().
                whereEqualTo(FirestoreReferences.HANDLEDBY_FIELD, username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> result = querySnapshot.getDocuments();
                        for(DocumentSnapshot ds:result) {
                            //TO REMEMBER: public ClassModel(String _id, String classCode, String sectionCode, String className, int studentCount, boolean isPublished)
                            classModels.add(new ClassModel(
                                    "0000",
                                    ds.get("courseCode").toString(),
                                    ds.get("sectionCode").toString(),
                                    ds.get("courseName").toString(),
                                    Integer.parseInt(ds.get("studentCount").toString()),
                                    Boolean.parseBoolean( ds.get("isPublished").toString() )));
                            //sets up the necessary courses

                            recyclerView = findViewById(R.id.recyclerView);

                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                            classlistAdapter = new ClasslistAdapter(classModels);
                            recyclerView.setAdapter(classlistAdapter);

                        }
                    }
                });

        //to be used once sir replies to our email inquiry

        //this.classModels = new ClassDataHelper().initializeData();
        //setupRecyclerView();
    }

    //to be used once sir replies to our email inquiry
    void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.recyclerView);

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        this.classlistAdapter = new ClasslistAdapter(this.classModels);
        this.recyclerView.setAdapter(this.classlistAdapter);
    }

}