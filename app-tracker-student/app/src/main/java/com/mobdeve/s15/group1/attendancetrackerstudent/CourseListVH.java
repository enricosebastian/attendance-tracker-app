package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class CourseListVH extends RecyclerView.ViewHolder {
    private static final String TAG = "ClasslistVH.java";
    private TextView txtClassCode, txtSectionCode;

    public CourseListVH(@NonNull View itemView) {
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

    public TextView getTxtClassCode() {
        return txtClassCode;
    }

    public TextView getTxtSectionCode() {
        return txtSectionCode;
    }
}
