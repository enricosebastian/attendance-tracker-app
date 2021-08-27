package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private static String SP_EMAIL_KEY = "SP_EMAIL_KEY";
    private static String SP_USERNAME_KEY = "SP_USERNAME_KEY";

    private static String USERNAME_STATE_KEY = "USERNAME_KEY";
    private static String EMAIL_STATE_KEY = "EMAIL_KEY";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClasslistAdapter classlistAdapter;
    private DocumentSnapshot donny;

    private TextView txtName;
    private TextView txtIdNumber;
    private FloatingActionButton btnSearchCourse;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list_view);

        this.sp = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();

        this.db = FirebaseFirestore.getInstance();

        Intent passedIntent = getIntent();
        String username = passedIntent.getStringExtra(USERNAME_STATE_KEY);
        String email = passedIntent.getStringExtra(EMAIL_STATE_KEY);

        this.txtName = findViewById(R.id.tvName);
        this.txtIdNumber = findViewById(R.id.tvIdName);
        this.btnSearchCourse = findViewById(R.id.btnSearchCourse);

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


        //go to search course activity
        btnSearchCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ClasslistView.this, SearchCourseActivity.class);
                startActivity(intent);
            }
        });
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
                editor.putString(SP_EMAIL_KEY,"");
                editor.putString(SP_USERNAME_KEY,"");
                editor.commit();

                Intent intentLogout = new Intent (ClasslistView.this, LoginView.class);
                startActivity(intentLogout);
                return true;
            default:
                return false;
        }
    }
}