package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AcceptStudentsVH extends RecyclerView.ViewHolder {

    private ImageView ivDisplayPicture;
    private TextView tvStudentName;
    //private Button btnConfirm, btnDelete;

    /** This constructor initializes the views for the Accept Student recycler view */
    public AcceptStudentsVH (@NonNull View itemView) {
        super(itemView);
        this.ivDisplayPicture = itemView.findViewById(R.id.ivDisplayPicture);
        this.tvStudentName = itemView.findViewById(R.id.tvStudentName);
        this.btnConfirm = itemView.findViewById(R.id.tvStudentName);
        this.btnDelete = itemView.findViewById(R.id.btnDelete);
    }

    /**
     *  This method sets the value for the display picture
     * @param ivDisplayPicture the display picture of a particular student
     * */
    public void setIvDisplayPicture(int ivDisplayPicture) {
        this.ivDisplayPicture.setImageResource(ivDisplayPicture);
    }

    /**
     * This method sets the value for the name of the student requesting to join a course
     * @param tvStudentName the name of the student
     * */
    public void setTvStudentName(String tvStudentName) {
        this.tvStudentName.setText(tvStudentName);
    }

}
