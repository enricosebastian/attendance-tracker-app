package com.mobdeve.s15.group1.attendancetrackerstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleMeetingActivity extends AppCompatActivity {

    private static final String     TAG = "SingleMeetingActivity";

    //shared preferences initialization
    private SharedPreferences       sp;
    private String                  email;
    ////////////

    //widget initialization
    private EditText        inputMeetingCode;
    private Button          btnAttend;
    private TextView        txtStatus,
                            txtDate,
                            txtClassTitle,
                            txtAttendanceStatus,
                            txtClassNameSubtitle;

    private String          courseCode,
                            sectionCode,
                            meetingCode,
                            courseName;
    private boolean         isOpen; //something to note: what happens if you change boolean (primitive) to Boolean instead?
    private Date            meetingStart;
    private ProgressDialog  progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_meeting_view);

        this.sp             = getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.email          = sp.getString(Keys.SP_EMAIL_KEY, "");

        Intent getIntent    = getIntent();
        this.courseCode     = getIntent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode    = getIntent.getStringExtra(Keys.INTENT_SECTIONCODE);
        this.meetingCode    = getIntent.getStringExtra(Keys.INTENT_MEETINGCODE);
        this.isOpen         = getIntent.getBooleanExtra(Keys.INTENT_ISOPEN, false);
        String stringDate   = getIntent.getStringExtra(Keys.INTENT_MEETINGSTART);

        //initialize progress dialog so it can be called anywhere in the class
        this.progressDialog = new ProgressDialog(SingleMeetingActivity.this);

        Log.d(TAG, meetingCode);

        try {
            meetingStart = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.txtStatus                  = findViewById(R.id.txtStatus);
        this.txtDate                    = findViewById(R.id.txtDate);
        this.txtClassTitle              = findViewById(R.id.txtClassCodeTitle);
        this.txtClassNameSubtitle       = findViewById(R.id.txtClassNameSubtitle);
        this.txtAttendanceStatus        = findViewById(R.id.txtAttendanceStatus);
        this.inputMeetingCode           = findViewById(R.id.inputMeetingCode);
        this.btnAttend                  = findViewById(R.id.btnAttend);


        //if student clicks button, activate attendance recording functionality
        btnAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAttend.getText().toString().equals("RECORDED")) {
                    Toast.makeText(getApplicationContext(), "Already recorded!", Toast.LENGTH_SHORT).show();
                } else if(btnAttend.getText().toString().equals("CLOSED")) {
                    Toast.makeText(getApplicationContext(), "Meeting is still closed!", Toast.LENGTH_SHORT).show();
                } else {
                    if(inputMeetingCode.getText().toString().equals(meetingCode)) {
                        recordAttendance();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong meeting code!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //method to record attendance by getting email field, and validating student history
    protected void recordAttendance() {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, email).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> usersResult = Db.getDocuments(task);
                if(usersResult.size() != 0) {

                    Map<String, Object> input = new HashMap<>();

                    input.put(Db.FIELD_COURSECODE, courseCode);
                    input.put(Db.FIELD_FIRSTNAME, usersResult.get(0).getString(Db.FIELD_FIRSTNAME));
                    input.put(Db.FIELD_ISPRESENT, true);
                    input.put(Db.FIELD_LASTNAME, usersResult.get(0).getString(Db.FIELD_LASTNAME));
                    input.put(Db.FIELD_MEETINGCODE, meetingCode);
                    input.put(Db.FIELD_SECTIONCODE, sectionCode);
                    input.put(Db.FIELD_STUDENTATTENDED, email);

                    Db.addDocument(Db.COLLECTION_MEETINGHISTORY, input);

                    btnAttend.setText("RECORDED");
                    btnAttend.setBackgroundTintList(getResources().getColorStateList(R.color.light_gray));
                    btnAttend.setTextColor(Color.BLACK);

                    inputMeetingCode.setEnabled(false);
                    inputMeetingCode.setFocusable(false);
                    inputMeetingCode.setText(meetingCode);

                    Toast.makeText(getApplicationContext(), "Attendance successfully recorded!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // This method initializes the view of the activity
    protected void initializeViews() {
        //Show Progress bar
        this.progressDialog.setMessage("Loading...");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy | E");
        txtDate.setText(dateFormat.format(meetingStart));

        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_COURSECODE, courseCode).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                courseName = result.get(0).getString(Db.FIELD_COURSENAME);
                String classCodeTitle = courseCode + " - " + sectionCode;
                String classNameSubtitle = courseName;
                txtClassTitle.setText(classCodeTitle);
                txtClassNameSubtitle.setText(classNameSubtitle);

                // Checks the status adjusts the background based on the current status
                if(isOpen) {
                    txtStatus.setText("OPEN");
                    txtStatus.setBackgroundTintList(getResources().getColorStateList(R.color.light_green));
                } else {
                    txtStatus.setText("CLOSED");
                    txtStatus.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));

                    btnAttend.setText("CLOSED");
                    btnAttend.setBackgroundTintList(getResources().getColorStateList(R.color.light_gray));
                    btnAttend.setTextColor(Color.BLACK);

                    inputMeetingCode.setEnabled(false);
                    inputMeetingCode.setFocusable(false);
                }

                Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
                Db.FIELD_MEETINGCODE, meetingCode,
                Db.FIELD_STUDENTATTENDED, email).
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> result = Db.getDocuments(task);
                        if(result.size()==0) {
                            txtAttendanceStatus.setText("ATTENDANCE NOT RECORDED");
                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.dismiss();
                        } else {
                            if(!result.get(0).getBoolean(Db.FIELD_ISPRESENT)) {
                                txtAttendanceStatus.setText("ATTENDANCE NOT RECORDED");

                                btnAttend.setText("ATTEND");
                                btnAttend.setBackgroundTintList(getResources().getColorStateList(R.color.light_green));
                                btnAttend.setTextColor(Color.WHITE);

                                inputMeetingCode.setEnabled(true);
                                inputMeetingCode.setFocusable(true);
                                inputMeetingCode.setText("");

                                progressDialog.setCanceledOnTouchOutside(true);
                                progressDialog.dismiss();
                            } else {
                                txtAttendanceStatus.setText("ATTENDANCE RECORDED");

                                btnAttend.setText("RECORDED");
                                btnAttend.setBackgroundTintList(getResources().getColorStateList(R.color.light_gray));
                                btnAttend.setTextColor(Color.BLACK);

                                inputMeetingCode.setEnabled(false);
                                inputMeetingCode.setFocusable(false);
                                inputMeetingCode.setText(meetingCode);

                                progressDialog.setCanceledOnTouchOutside(true);
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
    }

    protected void onResume() {
        super.onResume();
        initializeViews();
    }

}