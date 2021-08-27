package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;

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

        Task<QuerySnapshot> test = FirestoreReferences.getStudentsInMeeting("MOBDEVE+S12+0002");
        test.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = FirestoreReferences.toList(task);
                Log.d("in here lmao",""+result.size());
            }
        });

    }
}