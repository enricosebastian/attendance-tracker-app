package com.mobdeve.s15.group1.attendancetrackerstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.storage.OnObbStateChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Values;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchCourseActivity extends AppCompatActivity {

    private final static String TAG = "SearchCourseActivity";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String email;

    private ConstraintLayout resultBox;

    private EditText inputCourseCode, inputSectionCode;
    private TextView txtCourseInfo, txtHandledBy;
    private Button btnSearch, btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.editor         = sp.edit();
        this.email          = sp.getString(Keys.SP_EMAIL_KEY,"");


        this.inputCourseCode = findViewById(R.id.inputCourseCode);
        this.inputSectionCode = findViewById(R.id.inputSectionCode);
        this.btnSearch = findViewById(R.id.btnSearch);
        this.resultBox = findViewById(R.id.resultBox);
        this.txtCourseInfo = findViewById(R.id.txtCourseInfo);
        this.txtHandledBy = findViewById(R.id.txtHandledBy);
        this.btnRequest     = findViewById(R.id.btnRequest);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = inputCourseCode.getText().toString().toUpperCase();
                String sectionCode = inputSectionCode.getText().toString().toUpperCase();
                if(courseCode.isEmpty() || sectionCode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "One of the input fields is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    searchQuery(courseCode, sectionCode);
                }
            }
        });
        
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnRequest.getText().toString().equals("SENT")) {
                    Toast.makeText(getApplicationContext(), "Request already sent!", Toast.LENGTH_SHORT).show();
                } else {
                    String courseCode = inputCourseCode.getText().toString().toUpperCase();
                    String sectionCode = inputSectionCode.getText().toString().toUpperCase();
                    sendRequest(courseCode, sectionCode);
                    btnRequest.setText("SENT");
                    btnRequest.setBackgroundColor(Color.GRAY);
                }
            }
        });
    }

    protected void searchQuery(String courseCode, String sectionCode) {
        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_ISPUBLISHED, true).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                if(result.size() == 0) {
                    resultBox.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Course does not exist!", Toast.LENGTH_SHORT).show();
                } else {
                    resultBox.setVisibility(View.VISIBLE);

                    Db.getDocumentsWith(Db.COLLECTION_USERS,
                    Db.FIELD_EMAIL, result.get(0).getString(Db.FIELD_HANDLEDBY)).
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<DocumentSnapshot> userResults = Db.getDocuments(task);
                            txtCourseInfo.setText(courseCode+ " - " +sectionCode); //the easy way out. i am ashamed of myself, but fuck you still
                            txtHandledBy.setText("Handled by "+userResults.get(0).getString(Db.FIELD_FIRSTNAME)+ " "+userResults.get(0).getString(Db.FIELD_LASTNAME));
                            checkIfResultExists(courseCode, sectionCode);
                        }
                    });
                }
            }
        });
    }

    protected void checkIfResultExists(String courseCode, String sectionCode) {

        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> studentInfoResult = Db.getDocuments(task);

                Db.getDocumentsWith(Db.COLLECTION_COURSEREQUEST,
                Db.FIELD_IDNUMBER, studentInfoResult.get(0).getString(Db.FIELD_IDNUMBER),
                Db.FIELD_COURSECODE, courseCode,
                Db.FIELD_SECTIONCODE, sectionCode).
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> courseRequestResult = Db.getDocuments(task);
                        if(courseRequestResult.size() != 0) {
                            btnRequest.setBackgroundColor(Color.LTGRAY);
                            btnRequest.setText("SENT");
                        } else {
                            btnRequest.setBackgroundColor(Color.GREEN);
                            btnRequest.setText("REQUEST");
                        }
                    }
                });
            }
        });
    }

    protected void sendRequest(String courseCode, String sectionCode) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> studentInfoResult = Db.getDocuments(task);

                HashMap<String, Object> input = new HashMap<>();
                input.put(Db.FIELD_COURSECODE, courseCode);
                input.put(Db.FIELD_FIRSTNAME, studentInfoResult.get(0).getString(Db.FIELD_FIRSTNAME));
                input.put(Db.FIELD_IDNUMBER, studentInfoResult.get(0).getString(Db.FIELD_IDNUMBER));
                input.put(Db.FIELD_LASTNAME, studentInfoResult.get(0).getString(Db.FIELD_LASTNAME));
                input.put(Db.FIELD_SECTIONCODE, sectionCode);

                Db.addDocument(Db.COLLECTION_COURSEREQUEST, input);
                inputSectionCode.setText("");
                inputCourseCode.setText("");
            }
        });
    }
}