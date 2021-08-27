package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.view.View;
import android.webkit.WebView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleMeetingVH extends RecyclerView.ViewHolder{

    private static final String TAG = "You are in SingleMeetingVH";
    private Switch switchIsPresent;
    private TextView txtStudentName;

    public SingleMeetingVH(@NonNull View itemView) {
        super(itemView);
        this.switchIsPresent = itemView.findViewById(R.id.switchIsPresent);
        this.txtStudentName = itemView.findViewById(R.id.txtStudentName);
    }

    public void setSwitchIsPresent(boolean isPresent) {
        this.switchIsPresent.setChecked(isPresent);
    }

    public void setTxtStudentName(String studentFirstName, String studentLastName) {
        this.txtStudentName.setText(studentLastName+", "+studentFirstName);
    }
}
