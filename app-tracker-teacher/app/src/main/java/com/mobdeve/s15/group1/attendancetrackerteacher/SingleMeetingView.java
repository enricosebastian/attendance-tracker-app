package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SingleMeetingView extends AppCompatActivity {
    
    private TextView txtStatus;
    private TextView txtDate;
    private TextView txtClassTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_meeting_view);

        Intent intent = getIntent();

        this.txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("DATE_KEY"));

        String courseCode = intent.getStringExtra("COURSECODE_KEY");
        String sectionCode = intent.getStringExtra("SECTIONCODE_KEY");

        this.txtClassTitle = findViewById(R.id.txtClassTitle);
        txtClassTitle.setText(courseCode +" - "+sectionCode);

        //txtClassTitle.setText(intent.getStringExtra("TXTCLASSTITLE_KEY").toString());
        
        this.txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtStatus.getText().toString().equals("CLOSED")) {
                    txtStatus.setText("OPEN");
                    txtStatus.setBackgroundTintList(v.getContext().getResources().getColorStateList(R.color.light_green));
                } else if(txtStatus.getText().toString().equals("OPEN")) {
                    txtStatus.setText("CLOSED");
                    txtStatus.setBackgroundTintList(v.getContext().getResources().getColorStateList(R.color.red_light));

                }
            }
        });

    }
}