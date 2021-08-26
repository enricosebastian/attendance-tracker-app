package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import org.w3c.dom.Document;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class ClasslistView extends AppCompatActivity {

    private ArrayList<ClassModel> classModels = new ArrayList<>();

    private static String SP_FILE_NAME = "LoginPreferences";
    private static String EMAIL_STATE_KEY = "EMAIL_KEY";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClasslistAdapter classlistAdapter;

    private TextView txtName;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.db = FirebaseFirestore.getInstance();

        Intent passedIntent = getIntent();
        String email = passedIntent.getStringExtra(EMAIL_STATE_KEY);

        this.txtName = findViewById(R.id.txtName);

        CollectionReference collectionRef = db.collection(FirestoreReferences.USERS_COLLECTION);
        Query query = collectionRef.whereEqualTo(FirestoreReferences.EMAIL_FIELD, email);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> result = querySnapshot.getDocuments();
                Log.d("GOT THIS: ",result.get(0).get(FirestoreReferences.USERTYPE_FIELD).toString());
                String fullName =
                        result.get(0).get(FirestoreReferences.FIRSTNAME_FIELD).toString() + " " +
                        result.get(0).get(FirestoreReferences.FIRSTNAME_FIELD).toString();
                txtName.setText(fullName);
            }
        });

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