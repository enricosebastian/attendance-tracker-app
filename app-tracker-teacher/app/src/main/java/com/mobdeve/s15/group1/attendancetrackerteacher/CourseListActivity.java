package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/*
    This activity is launched right after a teacher logs in. All of the teacher's course
    has an option for it to be Edited or Deleted. There is also a button for adding a new
    course. On the header of the view are some credential information and options to
    Edit profile, change Account security, and Logout.
 */
public class CourseListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String             TAG = "ClasslistActivity.java";

    //shared preferences initialization
    private SharedPreferences               sp;
    private SharedPreferences.Editor        editor;
    private String                          email;

    //recycler view initialization
    private ArrayList<CourseModel>          courseModels = new ArrayList<>();
    private RecyclerView                    courseListRecyclerView;
    private RecyclerView.LayoutManager      courseListLayoutManager;
    private CourseListAdapter               courseListAdapter;

    //widget initialization
    private TextView                txtName,
                                    txtIdNumber;
    private FloatingActionButton    btnAddCourse;
    private ImageView               imgProfilePic;
    private SwipeRefreshLayout      refreshLayout;

    private ProgressDialog progressDialog;

    // What is being returned after the adding another course
    private ActivityResultLauncher<Intent> editProfileInfoLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "Add success so reinitialize the views.");
                    initializeViews();
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
        this.btnAddCourse   = findViewById(R.id.btnAddCourse);
        this.imgProfilePic  = findViewById(R.id.img_profilePic);
        this.refreshLayout  = findViewById(R.id.refreshLayout);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(CourseListActivity.this);


        // When user clicks on add course button
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createCourseIntent = new Intent(CourseListActivity.this, CreateCourseActivity.class);
                editProfileInfoLauncher.launch(createCourseIntent);
            }
        });

        // When user refreshes the page
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeRecyclerView();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    //Handles the pop up button for the profile tab
    public void showPopup (View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }

    // When user clicks on one of the popup menu items
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.editProfile:
                Intent editProfileIntent = new Intent (CourseListActivity.this, EditProfileActivity.class);
                editProfileInfoLauncher.launch(editProfileIntent);
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

    //Initialize views of the courses handled by the user
    protected void initializeViews() {
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                Log.d(TAG,"result size is "+ email);
                Log.d(TAG,"result size is "+ result.size());

                String firstName = result.get(0).getString(Db.FIELD_FIRSTNAME).toUpperCase();
                String lastName = result.get(0).getString(Db.FIELD_LASTNAME).toUpperCase();

                String idNumber = result.get(0).getString(Db.FIELD_IDNUMBER);
                txtName.setText(firstName+" "+lastName);
                txtIdNumber.setText(idNumber);

                // To get user's profile picture
                String documentId = Db.getIdFromTask(task);
                Db.getProfilePic(documentId).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Task successful.");
                            Uri imgUri = task.getResult();
                            Picasso.get().load(imgUri).into(imgProfilePic);
                            initializeRecyclerView(); //recycler view gets initialized when task is complete
                        } else {
                            Log.d(TAG,"No profile image found. Switching to default");
                            initializeRecyclerView(); //recycler view gets initialized when task is complete
                        }
                    }
                });

            }
        });
    }

    // Initializes the recycler view of course list activity
    protected void initializeRecyclerView() {
        Log.d(TAG, "Initializing recycling view.");

        //Gets the courses handled by the user
        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_HANDLEDBY, email,
        Db.FIELD_COURSECODE, Query.Direction.ASCENDING,
        Db.FIELD_SECTIONCODE, Query.Direction.ASCENDING).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                courseModels.clear(); //always clear when initializing
                courseModels.addAll(Db.toClassModel(Db.getDocuments(task)));
                Log.d(TAG,"classModel size is "+ courseModels.size());
                courseListRecyclerView = findViewById(R.id.recyclerView);
                courseListLayoutManager = new LinearLayoutManager(getApplicationContext());
                courseListRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                courseListAdapter = new CourseListAdapter(courseModels);
                courseListRecyclerView.setAdapter(courseListAdapter);

                //Dismiss the loading dialog once everything is finished
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.dismiss();
            }
        });
    }

    // Initializes views onResume of the app
    protected void onResume() {
        super.onResume();
        initializeViews();
        Log.d(TAG,"we are in on resume");
    }
}
