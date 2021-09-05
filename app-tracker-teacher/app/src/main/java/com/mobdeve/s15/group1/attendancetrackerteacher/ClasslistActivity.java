package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClasslistActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "ClasslistActivity.java";

    private ArrayList<ClassModel> classModels = new ArrayList<>();

    //shared preferences initialization
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;
    ////////////

    //recycler view initialization
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClasslistAdapter classlistAdapter;
    ////////////

    //widget initialization
    private TextView txtName;
    private TextView txtIdNumber;
    private FloatingActionButton btnAddCourse;
    private ImageView imgProfilePic;
    ////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor         = sp.edit();
        this.email          = sp.getString(Keys.SP_EMAIL_KEY,"");

        this.txtName        = findViewById(R.id.tvName);
        this.txtIdNumber    = findViewById(R.id.tvIdName);
        this.btnAddCourse   = findViewById(R.id.btnAddCourse);
        this.imgProfilePic  = findViewById(R.id.img_profilePic);


        Log.d(TAG,"received: "+email);
        initializeViews(email);

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClasslistActivity.this, CreateCourse.class);
                startActivity(intent);
            }
        });
    }

    //handles the pop up button
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
                Intent intent = new Intent (ClasslistActivity.this, EditProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.accountSecurity:
                Log.d(TAG,"account security selected");
                return true;
            case R.id.logout:
                editor.clear();
                editor.commit();
                Intent logoutIntent = new Intent (ClasslistActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                return true;
            default:
                return false;
        }
    }

    //initializes views
    protected void initializeViews(String email) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
            Db.FIELD_EMAIL, email).
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<DocumentSnapshot> result = Db.getDocuments(task);
                    String firstName = result.get(0).getString(Db.FIELD_FIRSTNAME);
                    String lastName = result.get(0).getString(Db.FIELD_LASTNAME);
                    String idNumber = result.get(0).getString(Db.FIELD_IDNUMBER);
                    txtName.setText(firstName+" "+lastName);
                    txtIdNumber.setText(idNumber);

                    String documentId = Db.getIdFromTask(task);
                    Db.getProfilePic(documentId).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri imgUri = task.getResult();
                            Picasso.get().load(imgUri).into(imgProfilePic);
                        }
                    });
                }
            });

        Db.getDocumentsWith(Db.COLLECTION_COURSES,
            Db.FIELD_HANDLEDBY, email).
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    classModels.clear(); //always clear when initializing

                    classModels.addAll(Db.toClassModel(Db.getDocuments(task)));
                    Log.d(TAG,"classModel size is "+classModels.size());

                    recyclerView = findViewById(R.id.recyclerView);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    classlistAdapter = new ClasslistAdapter(classModels);
                    recyclerView.setAdapter(classlistAdapter);
                }
            });
    }//initializes views

    //you can delete these
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "we are in on stop");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"we are in on destroy");
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG,"we are in on resume");
    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG, "we are in on start");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "we are in on pause");
    }
}
