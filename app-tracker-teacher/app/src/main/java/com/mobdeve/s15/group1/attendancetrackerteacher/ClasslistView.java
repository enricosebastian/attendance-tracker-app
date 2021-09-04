package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.View;

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
    private FloatingActionButton btnAddCourse;
    private FirebaseFirestore db;

    private ImageView imgTest;

    private static String previousUsernameEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.sp = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();

        this.previousUsernameEntry = sp.getString(SP_USERNAME_KEY, "");

        this.imgTest = findViewById(R.id.img_profilePic);

        this.db = FirebaseFirestore.getInstance();

        Intent passedIntent = getIntent();
        String username = passedIntent.getStringExtra(USERNAME_STATE_KEY);
        String email = passedIntent.getStringExtra(EMAIL_STATE_KEY);

        this.txtName = findViewById(R.id.tvName);
        this.txtIdNumber = findViewById(R.id.tvIdName);
        this.btnAddCourse = findViewById(R.id.btnAddCourse);

        Db.getUsersCollectionReference().
                whereEqualTo(Db.EMAIL_FIELD, email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> result = querySnapshot.getDocuments();
                        String fullName =
                                result.get(0).get(Db.FIRSTNAME_FIELD).toString() + " " +
                                result.get(0).get(Db.FIRSTNAME_FIELD).toString();
                        txtName.setText(fullName);
                        txtIdNumber.setText(result.get(0).get(Db.IDNUMBER_FIELD).toString());
                    }
                });

//        upload image
//        Query q = FirestoreReferences.findDocuments(FirestoreReferences.USERS_COLLECTION,FirestoreReferences.USERNAME_FIELD,previousUsernameEntry);
//        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                String id = FirestoreReferences.getIdFromTask(task);
//
//                Task<Uri> getImageTask = FirestoreReferences
//                    .getImageUri(id)
//                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            Uri imgUri = task.getResult();
//                            Picasso.get().load(imgUri).into(imgTest);
//                        }
//                    });
//            }
//        });


//        StorageReference fs = FirebaseStorage.getInstance().getReference();
//        Task <Uri> taskUri = fs.child("thanos").getDownloadUrl();
//        taskUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                Uri test = task.getResult()
//                //Picasso.get().load(test).into(imgTest);
//            }
//        });

        Db.getCoursesCollectionReference().
                whereEqualTo(Db.HANDLEDBY_FIELD, username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> result = querySnapshot.getDocuments();

                        classModels = Db.toClassModel(result);

                        recyclerView = findViewById(R.id.recyclerView);

                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

                        classlistAdapter = new ClasslistAdapter(classModels);
                        recyclerView.setAdapter(classlistAdapter);
                    }
                });

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClasslistView.this, CreateCourse.class);
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
                editor.clear();
                editor.commit();
                Intent intentLogout = new Intent (ClasslistView.this, LoginView.class);
                startActivity(intentLogout);
                return true;
            default:
                return false;
        }
    }
}