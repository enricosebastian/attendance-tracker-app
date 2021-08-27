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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);

        this.sp = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sp.edit();

        this.imgTest = findViewById(R.id.img_profilePic);

        StorageReference fs = FirebaseStorage.getInstance().getReference();
        Task <Uri> taskUri = fs.child("thanos").getDownloadUrl();
        taskUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri test = task.getResult();
                Picasso.get().load(test).into(imgTest);
            }
        });

        //Picasso.get().load(url).into(imgTest);


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

        FirestoreReferences.getCoursesCollectionReference().
                whereEqualTo(FirestoreReferences.HANDLEDBY_FIELD, username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> result = querySnapshot.getDocuments();

                        Log.d("test","look at these class counts: "+result.get(0).get("studentCount").toString());
                        classModels = FirestoreReferences.toClassModel(result);

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
                //Toast.makeText(v.getContext(), "adding class...", Toast.LENGTH_SHORT).show();
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