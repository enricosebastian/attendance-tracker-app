package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClasslistVH extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "Class ViewHolder";
    private TextView txtClassCode, txtSectionCode;
    private ImageButton btnMoreOptions;

    public ClasslistVH(@NonNull View itemView) {
        super(itemView);
        this.txtClassCode = itemView.findViewById(R.id.txtClassCode);
        this.txtSectionCode = itemView.findViewById(R.id.txtSectionCode);
        //this.btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
        //this.btnMoreOptions.setOnClickListener(this);
    }

    public void setTxtClassCode(String txtClassCode) {
        this.txtClassCode.setText(txtClassCode);
    }

    public void setTxtSectionCode(String txtName) {
        this.txtSectionCode.setText(txtName);
    }


    public void setBtnMoreOptions(String txtName) {
        this.txtSectionCode.setText(txtName);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onCLick: " + getAdapterPosition());
        showPopupMenu(v);
    }

    //  Attribution: https://youtu.be/hKyjb4b19YM
    private void showPopupMenu(View view)  {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.course_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editCourse:
                Log.d(TAG, "Action edit course @ position: " + getAdapterPosition());
                Intent intent = new Intent();

                return true;
            case R.id.deleteCourse:
                Log.d(TAG, "Action delete course @ position: " + getAdapterPosition());
                return true;
            default:
                return false;
        }

    }
}
