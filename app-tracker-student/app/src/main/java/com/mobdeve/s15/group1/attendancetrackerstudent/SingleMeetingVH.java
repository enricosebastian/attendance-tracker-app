//package com.mobdeve.s15.group1.attendancetrackerstudent;
//
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class SingleMeetingVH extends RecyclerView.ViewHolder{
//
//    private static final String TAG = "You are in SingleMeetingVH";
//    private ImageButton btnIsPresent;
//    private TextView txtStudentName;
//
//    public SingleMeetingVH(@NonNull View itemView) {
//        super(itemView);
//        this.btnIsPresent = itemView.findViewById(R.id.isPresent);
//        this.txtStudentName = itemView.findViewById(R.id.txtMeetingStudentName);
//    }
//
//
//    public ImageButton getBtnIsPresent() {
//        return btnIsPresent;
//    }
//
//    // For changing the view if the user is present or not
//    public void setBtnIsPresent(boolean isPresent) {
//        if (isPresent) {
//            this.btnIsPresent.setImageResource(R.drawable.ic_present_foreground);
//        } else {
//            this.btnIsPresent.setImageResource(R.drawable.ic_absent_foreground);
//        }
//        this.btnIsPresent.setTag(isPresent);
//    }
//
//    //To check the current status of the student in a meeting
//    public boolean getIsPresent () {
//        return (boolean) (this.btnIsPresent.getTag());
//    }
//
//    //Sets the text of the student name in a single meeting view
//    public void setTxtStudentName(String studentFirstName, String studentLastName) {
//        String displayName = studentLastName+", "+studentFirstName;
//        this.txtStudentName.setText(displayName);
//    }
//}
