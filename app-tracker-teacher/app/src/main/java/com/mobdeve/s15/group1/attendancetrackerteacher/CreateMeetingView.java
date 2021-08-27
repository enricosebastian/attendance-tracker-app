package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

public class CreateMeetingView extends AppCompatActivity {
    private String courseCode, sectionCode;

    private TextView tvDisplayDate;
    private DatePickerDialog.OnDateSetListener dsl;
    private int meetingMonth, meetingDay, meetingYear;

    private Button btnCancel, btnCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting_view);
        this.btnCancel = findViewById(R.id.btnCancelCreateMeeting);
        this.btnCreate = findViewById(R.id.btnConfirmCreateMeeting);
        this.tvDisplayDate = findViewById(R.id.tvSelectDate);

        Intent intent = getIntent();
        this.courseCode = intent.getStringExtra("COURSECODE");
        this.sectionCode = intent.getStringExtra("SECTIONCODE");

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
        this.tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        CreateMeetingView.this,
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

                Date inputDate = new Date(meetingYear, meetingMonth, meetingDay);

                Timestamp parsedDate = new Timestamp(inputDate);
                Toast.makeText(v.getContext(),
                        parsedDate.toDate().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}