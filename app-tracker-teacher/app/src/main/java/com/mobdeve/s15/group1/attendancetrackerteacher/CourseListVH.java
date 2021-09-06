package com.mobdeve.s15.group1.attendancetrackerteacher;

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

public class CourseListVH extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "ClasslistVH.java";
    private TextView txtClassCode, txtSectionCode;
    private ImageButton btnMoreOptions;

    public CourseListVH(@NonNull View itemView) {
        super(itemView);
        this.txtClassCode = itemView.findViewById(R.id.txtClassCode);
        this.txtSectionCode = itemView.findViewById(R.id.txtSectionCode);
        this.btnMoreOptions = itemView.findViewById(R.id.btnMoreOption);
        this.btnMoreOptions.setOnClickListener(this);
    }


    public void setTxtClassCode(String txtClassCode) {
        this.txtClassCode.setText(txtClassCode);
    }


    public void setTxtSectionCode(String txtName) {
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
                Intent intent = new Intent(itemView.getContext(), EditCourseActivity.class);
                intent.putExtra(Keys.INTENT_COURSECODE, txtClassCode.getText().toString());
                intent.putExtra(Keys.INTENT_SECTIONCODE, txtSectionCode.getText().toString());
                itemView.getContext().startActivity(intent);
                return true;
            case R.id.deleteCourse:
                Log.d(TAG, "Action delete course @ position: " + getAdapterPosition());
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Delete course")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //deletes all info from courses
                                Db.deleteDocument(Db.COLLECTION_COURSES,
                                Db.FIELD_SECTIONCODE, txtSectionCode.getText().toString(),
                                Db.FIELD_COURSECODE, txtClassCode.getText().toString()); //DAT NAMING INCONSISTENCY THO LMAO
                                Log.d(TAG,"deleted collection course");

                                //deletes all info from meetings
                                Db.deleteDocument(Db.COLLECTION_MEETINGS,
                                Db.FIELD_SECTIONCODE, txtSectionCode.getText().toString(),
                                Db.FIELD_COURSECODE, txtClassCode.getText().toString()); //DAT NAMING INCONSISTENCY THO LMAO
                                Log.d(TAG,"deleted collection meetings");
//
//                              //deletes all info from meeting history
                                Db.deleteDocument(Db.COLLECTION_MEETINGHISTORY,
                                Db.FIELD_SECTIONCODE, txtSectionCode.getText().toString(),
                                Db.FIELD_COURSECODE, txtClassCode.getText().toString()); //DAT NAMING INCONSISTENCY THO LMAO
                                Log.d(TAG,"deleted collection meeting history");

                                //how to update recycler view VVVVVVVVVVVVVVVVVV
                                //how to update recycler view VVVVVVVVVVVVVVVVVV
                                //how to update recycler view VVVVVVVVVVVVVVVVVV
                                //how to update recycler view VVVVVVVVVVVVVVVVVV
                                //how to update recycler view VVVVVVVVVVVVVVVVVV

                                //how to update recycler view ^^^^^^^^^^^^^^^^^^
                                //how to update recycler view ^^^^^^^^^^^^^^^^^^
                                //how to update recycler view ^^^^^^^^^^^^^^^^^^
                                //how to update recycler view ^^^^^^^^^^^^^^^^^^
                                //how to update recycler view ^^^^^^^^^^^^^^^^^^

                                Toast.makeText(itemView.getContext(), "Course has been successfully deleted", Toast.LENGTH_SHORT).show();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            default:
                return false;
        }

    }

}
