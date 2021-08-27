package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClasslistView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ArrayList<ClassModel> classModels = new ArrayList<>();

    private static String SP_FILE_NAME = "LoginPreferences";
    private static String USERNAME_STATE_KEY = "USERNAME_KEY";
    private static String EMAIL_STATE_KEY = "EMAIL_KEY";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClasslistAdapter classlistAdapter;
    private DocumentSnapshot donny;

    private TextView txtName;
    private TextView txtIdNumber;
    private FloatingActionButton btnAddCourse;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.db = FirebaseFirestore.getInstance();

        Intent passedIntent = getIntent();
        String username = passedIntent.getStringExtra(USERNAME_STATE_KEY);
        String email = passedIntent.getStringExtra(EMAIL_STATE_KEY);

        this.txtName = findViewById(R.id.tvName);
        this.txtIdNumber = findViewById(R.id.tvIdName);
        this.btnAddCourse = findViewById(R.id.btnAddCourse);

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
                        txtIdNumber.setText(result.get(0).get(FirestoreReferences.IDNUMBER_FIELD).toString());
                    }
                });

//        Task<QuerySnapshot> test = FirestoreReferences.getUsersWithEmail("ben@dlsu.edu.ph");
//        test.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                DocumentSnapshot res = FirestoreReferences.getFirstResult(task);
//                Log.d("what is res","res is: "+res);
//
//                Date test = res.getDate("timeTest");
//                Log.d("in void here",test.toString());
//
////                String sDate1 = res.get("timeTest").toString();
////                Log.d("what is sDate1",sDate1);
////                Timestamp test = new Timestamp(1630383132,0);
////                Log.d("in void here",test.toDate().toString());
//            }
//        });


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
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

                            classlistAdapter = new ClasslistAdapter(classModels);
                            recyclerView.setAdapter(classlistAdapter);

                        }
                    }
                });


        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ClasslistView.this, CreateCourse.class);
                startActivity(intent);
            }
        });
        //to be used once sir replies to our email inquiry

        //this.classModels = new ClassDataHelper().initializeData();
        //setupRecyclerView();
    }



   public void showPopup (View v) {
       PopupMenu popup = new PopupMenu(this, v);
       popup.setOnMenuItemClickListener(this);
       popup.inflate(R.menu.profile_menu);
       popup.show();

   }

   //Handles the clicks on the items
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.editProfile:
                Intent intent = new Intent (ClasslistView.this, EditProfile.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                Intent intentLogout = new Intent (ClasslistView.this, LoginView.class);
                startActivity(intentLogout);
                return true;
            default:
                return false;
        }
    }

    //    void setupRecyclerView() {
//        this.recyclerView = findViewById(R.id.recyclerView);
//
//        this.layoutManager = new LinearLayoutManager(this);
//        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//
//        this.classlistAdapter = new ClasslistAdapter(this.classModels);
//        this.recyclerView.setAdapter(this.classlistAdapter);
//    }

}