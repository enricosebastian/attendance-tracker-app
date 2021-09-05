package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateMeetingActivity extends AppCompatActivity {
    private static final String TAG = "CreateMeetingActivity";

    private String courseCode, sectionCode;

    private TextView tvDisplayDate;
    private DatePickerDialog.OnDateSetListener dsl;

    private int meetingMonth, meetingDay, meetingYear, meetingHour, meetingMinute;

    private Button btnCancel, btnCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting_view);
        this.btnCancel = findViewById(R.id.btnCancelCreateMeeting);
        this.btnCreate = findViewById(R.id.btnConfirmCreateMeeting);
        this.tvDisplayDate = findViewById(R.id.tvSelectDate);

        Intent intent = getIntent();
        this.courseCode = intent.getStringExtra(Keys.INTENT_COURSECODE);
        this.sectionCode = intent.getStringExtra(Keys.INTENT_SECTIONCODE);

        //clean this lmao
        dsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                String date = (month + 1) + "/" + day + "/" + year;

                meetingMonth = month;

                meetingDay = day;
                meetingYear = year;

                tvDisplayDate.setText(date);
            }
        };

        //clean this too lmao
        this.tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateMeetingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dsl,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //future feature
                meetingHour = 12; //to be dynamic if we have time
                meetingMinute = 45; //to be dynamic if we have time

                String dateString = (meetingMonth+1)+"."+meetingDay+"."+meetingYear+"-"+meetingHour+"."+meetingMinute;

                Date date = null;
                try {
                    date = new SimpleDateFormat("M.d.yyyy-HH.mm").parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Timestamp meetingStart = new Timestamp(date);
                createNewMeeting(meetingStart, dateString);
                Toast.makeText(getApplicationContext(), "new meeting created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    protected void createNewMeeting(Timestamp timestamp, String dateString) {
        Map<String, Object> input = new HashMap<>();

        input.put(Db.FIELD_COURSECODE, courseCode);
        input.put(Db.FIELD_MEETINGCODE, courseCode+"-"+sectionCode+"-"+dateString);
        input.put(Db.FIELD_MEETINGSTART, timestamp);
        input.put(Db.FIELD_SECTIONCODE, sectionCode);
        input.put(Db.FIELD_STUDENTCOUNT, 0);

        Db.addDocument(Db.COLLECTION_MEETINGS, input);
    }
}