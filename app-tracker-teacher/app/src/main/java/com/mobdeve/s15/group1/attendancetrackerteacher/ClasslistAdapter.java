package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClasslistAdapter extends RecyclerView.Adapter<ClasslistVH> {
    private static final String TAG = "ClasslistAdapter.java";

    //store data here
    private ArrayList<ClassModel> data;

    public ClasslistAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter is initialized");
    }

    @NonNull
    @Override
    public ClasslistVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_class, parent, false);
        ClasslistVH classlistVH = new ClasslistVH(view);

        classlistVH.setBtnMoreOptionsOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Action edit course @ position: " + classlistVH.getAdapterPosition());
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.course_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editCourse:
                                Log.d(TAG, "Action edit course @ position: " + classlistVH.getAdapterPosition());
//                                Intent intent = new Intent(itemView.getContext(), EditCourseActivity.class);
//                                intent.putExtra(Keys.INTENT_COURSECODE, txtClassCode.getText().toString());
//                                intent.putExtra(Keys.INTENT_SECTIONCODE, txtSectionCode.getText().toString());
//                                itemView.getContext().startActivity(intent);
                                return true;
                            case R.id.deleteCourse:
                                Log.d(TAG, "Action delete course @ position: " + classlistVH.getAdapterPosition());
                                new AlertDialog.Builder(classlistVH.itemView.getContext())
                                        .setTitle("Delete course")
                                        .setMessage("Are you sure you want to delete this entry?")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
//                                                //deletes all info from courses
//                                                Db.deleteDocument(Db.COLLECTION_COURSES,
//                                                        Db.FIELD_SECTIONCODE, txtSectionCode.getText().toString(),
//                                                        Db.FIELD_COURSECODE, txtClassCode.getText().toString()); //DAT NAMING INCONSISTENCY THO LMAO
//                                                Log.d(TAG,"deleted collection course");
//
//                                                //deletes all info from meetings
//                                                Db.deleteDocument(Db.COLLECTION_MEETINGS,
//                                                        Db.FIELD_SECTIONCODE, txtSectionCode.getText().toString(),
//                                                        Db.FIELD_COURSECODE, txtClassCode.getText().toString()); //DAT NAMING INCONSISTENCY THO LMAO
//                                                Log.d(TAG,"deleted collection meetings");
////
////                              //deletes all info from meeting history
//                                                Db.deleteDocument(Db.COLLECTION_MEETINGHISTORY,
//                                                        Db.FIELD_SECTIONCODE, txtSectionCode.getText().toString(),
//                                                        Db.FIELD_COURSECODE, txtClassCode.getText().toString()); //DAT NAMING INCONSISTENCY THO LMAO
//                                                Log.d(TAG,"deleted collection meeting history");

                                                Toast.makeText(classlistVH.itemView.getContext(), "Course has been successfully deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
        return classlistVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ClasslistVH holder, @SuppressLint("RecyclerView") int position) {
        holder.setTxtClassCode(data.get(position).getCourseCode());
        holder.setTxtSectionCode(data.get(position).getSectionCode());

        //When an item is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleClassViewIntent = new Intent(holder.itemView.getContext(), SingleClassActivity.class);

                singleClassViewIntent.putExtra(Keys.INTENT_COURSECODE, data.get(position).getCourseCode());
                singleClassViewIntent.putExtra(Keys.INTENT_SECTIONCODE, data.get(position).getSectionCode());
                singleClassViewIntent.putExtra(Keys.INTENT_COURSENAME, data.get(position).getCourseName());
                holder.itemView.getContext().startActivity(singleClassViewIntent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return data.size();
    }
}
