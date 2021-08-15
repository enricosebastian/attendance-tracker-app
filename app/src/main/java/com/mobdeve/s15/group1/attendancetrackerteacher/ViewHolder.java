package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "Class ViewHolder";
    private TextView txtClassCode, txtSectionCode;
    private ImageView imgContactPhoto;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.txtClassCode = itemView.findViewById(R.id.txtClassCode);
        this.txtSectionCode = itemView.findViewById(R.id.txtSectionCode);
    }

    public void setTxtClassCode(String txtClassCode) {
        this.txtClassCode.setText(txtClassCode);
    }

    public void setTxtSectionCode(String txtName) {
        this.txtSectionCode.setText(txtName);
    }

}
