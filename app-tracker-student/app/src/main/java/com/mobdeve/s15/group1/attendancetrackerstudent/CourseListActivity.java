package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "ClasslistActivity.java";

    //shared preferences initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;
    ////////////

    //recycler view initialization
    private RecyclerView courseListRecyclerView;
    private RecyclerView.LayoutManager courseListLayoutManager;
    private CourseListAdapter courseListAdapter;
    private ArrayList<CourseModel> courseModels = new ArrayList<>();
    ////////////

    //widget initialization
    private TextView txtName;
    private TextView txtIdNumber;
    private FloatingActionButton btnSearchCourse;
    private ImageView imgProfilePic;
    ////////////


    private ActivityResultLauncher<Intent> createCourseActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "Add success so reinitialize the views.");
                    //initializeViews();
                } else {
                    Log.d(TAG, "Nothing returned");
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courselist);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor         = sp.edit();
        this.email          = sp.getString(Keys.SP_EMAIL_KEY,"");

        this.txtName        = findViewById(R.id.tvName);
        this.txtIdNumber    = findViewById(R.id.tvIdName);
        this.btnSearchCourse = findViewById(R.id.btnSearchCourse);
        this.imgProfilePic  = findViewById(R.id.img_profilePic);

        //go to search course activity
        btnSearchCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CourseListActivity.this, SearchCourseActivity.class);
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
                Intent editProfileIntent = new Intent (CourseListActivity.this, EditProfileActivity.class);
                createCourseActivityResultLauncher.launch(editProfileIntent);
                return true;
            case R.id.editAccountSecurity:
                Intent editAccountSecurityIntent = new Intent (CourseListActivity.this, EditAccountSecurityActivity.class);
                startActivity(editAccountSecurityIntent);
                return true;
            case R.id.logout:
                editor.clear();
                editor.commit();
                Intent logoutIntent = new Intent (CourseListActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                return true;
            default:
                return false;
        }
    }

    protected void initializeViews() {

        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<DocumentSnapshot> result = Db.getDocuments(task);
                Log.d(TAG,"result size is "+email);
                Log.d(TAG,"result size is "+result.size());

                String firstName = result.get(0).getString(Db.FIELD_FIRSTNAME).toUpperCase();
                String lastName = result.get(0).getString(Db.FIELD_LASTNAME).toUpperCase();

                String idNumber = result.get(0).getString(Db.FIELD_IDNUMBER);
                txtName.setText(firstName+" "+lastName);
                txtIdNumber.setText(idNumber);

                String documentId = Db.getIdFromTask(task);
                Db.getProfilePic(documentId).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            Uri imgUri = task.getResult();
                            Picasso.get().load(imgUri).into(imgProfilePic);
                        } else {
                            Log.d(TAG,"No profile image found. Switching to default");
                        }
                    }
                });
            }
        });

        Db.getDocumentsWith(Db.COLLECTION_CLASSLIST,
        Db.FIELD_EMAIL, email).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                courseModels.clear();

                List<DocumentSnapshot> result = Db.getDocuments(task);
                for(DocumentSnapshot ds:result) {
                    Db.getDocumentsWith(Db.COLLECTION_COURSES,
                    Db.FIELD_COURSECODE, ds.getString(Db.FIELD_COURSECODE),
                    Db.FIELD_SECTIONCODE, ds.getString(Db.FIELD_SECTIONCODE)).
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            courseModels.addAll(Db.toCourseModel(Db.getDocuments(task)));
                            Log.d(TAG,"Class model size should be increasing --> "+courseModels.size());

                            courseListRecyclerView = findViewById(R.id.recyclerView);
                            courseListLayoutManager = new LinearLayoutManager(getApplicationContext());
                            courseListRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            courseListAdapter = new CourseListAdapter(courseModels);
                            courseListRecyclerView.setAdapter(courseListAdapter);
                        }
                    });
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
        initializeViews();
        Log.d(TAG,"we are in on resume");
    }

}