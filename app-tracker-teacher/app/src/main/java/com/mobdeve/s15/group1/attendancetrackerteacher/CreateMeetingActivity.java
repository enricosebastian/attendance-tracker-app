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
import java.util.Random;
/*
    Creating a meeting needs a date to be selected. The date cannot be a
    date before the current date.
 */
public class CreateMeetingActivity extends AppCompatActivity {
    private static final String TAG = "CreateMeetingActivity";

    private String courseCode, sectionCode;
    private int meetingMonth, meetingDay, meetingYear, meetingHour, meetingMinute;
    private boolean isDateSet = false;

    // widget initialization
    private TextView tvDisplayDate;
    private Button btnCancel, btnCreate;

    private DatePickerDialog.OnDateSetListener dsl;
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

        dsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                String date = (month + 1) + "/" + day + "/" + year;

                meetingMonth = month;

                meetingDay = day;
                meetingYear = year;
                isDateSet = true;

                tvDisplayDate.setText(date);
            }
        };

        // displays the Date Picker
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
                Log.d(TAG, "system time: " + System.currentTimeMillis() + ", cal time: " + cal.getTimeInMillis());
                // prevents selecting a date previous from the current date
                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 2000);
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

        // transforms the selected date into a Timestamp and adds it to the database
        this.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateSet)
                {
                    meetingHour = 12;
                    meetingMinute = 45;

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
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter a date!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // This calls the firebase function to create the meeting
    protected void createNewMeeting(Timestamp timestamp, String dateString) {
        Map<String, Object> input = new HashMap<>();

        input.put(Db.FIELD_COURSECODE, courseCode);
        input.put(Db.FIELD_MEETINGCODE, courseCode+"-"+sectionCode+"-"+genMeetingCode());
        input.put(Db.FIELD_MEETINGSTART, timestamp);
        input.put(Db.FIELD_SECTIONCODE, sectionCode);
        input.put(Db.FIELD_STUDENTCOUNT, 0);
        input.put(Db.FIELD_ISOPEN, false);
        Db.addDocument(Db.COLLECTION_MEETINGS, input);
    }


    /** This method generates a randomly generated password for the user.
     *  @return returns the randomly generated password of the user
     */
    public String genMeetingCode() {
        String possibleLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz"; //possible letters
        String possibleSpec = "0123456789" + "!@#$%^&*()_+{[}];:',./?"; //possible special character
        String possibleVals = possibleLetters + possibleSpec;

        int i, currIdx;
        Random randomize = new Random();
        int length = 6;

        char [] codeTemp = new char [length];

        for(i = 0; i < length /2; i++)
            codeTemp [i] = possibleLetters.charAt(randomize.nextInt(possibleLetters.length()));
        currIdx = i;
        for (i = currIdx; i < length /2+2; i++)//to ensure that there is always a special character
            codeTemp[i] = possibleSpec.charAt(randomize.nextInt( possibleSpec.length()));

        currIdx = i;
        for (i = currIdx; i < length ; i++)
            codeTemp[i] = possibleVals.charAt(randomize.nextInt(possibleVals.length()));

        String genCode = new String (codeTemp);
        codeTemp = null;

        return genCode;
    }
}