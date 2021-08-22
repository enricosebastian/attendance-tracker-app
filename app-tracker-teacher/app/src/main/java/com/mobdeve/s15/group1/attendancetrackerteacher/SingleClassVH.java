package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleClassVH extends RecyclerView.ViewHolder {
    private static final String TAG = "Class SingleClassViewHolder";
    private TextView txtStudentsPresent, txtDate;

    public SingleClassVH(@NonNull View itemView) {
        super(itemView);
        this.txtStudentsPresent = itemView.findViewById(R.id.txtStudentsPresent);
        this.txtDate = itemView.findViewById(R.id.txtDate);
    }

    public void setTxtStudentsPresent(int txtStudentsPresent) {
        this.txtStudentsPresent.setText(Integer.toString(txtStudentsPresent));
    }

    public void setTxtDate(String txtDate) {
        this.txtDate.setText(txtDate);
    }

}