package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListVH> {
    private static final String TAG = "ClasslistAdapter.java";

    //store data here
    private ArrayList<CourseModel> data;

    public CourseListAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter is initialized");
    }

    @NonNull
    @Override
    public CourseListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_class, parent, false);
        CourseListVH courseListVH = new CourseListVH(view);

        courseListVH.setBtnMoreOptionsOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(parent.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.course_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String courseCode = courseListVH.getTxtClassCode().getText().toString();
                        String sectionCode =  courseListVH.getTxtSectionCode().getText().toString();
                        Log.d(TAG,"course and sec: " + courseCode + " " + sectionCode);
                        switch (item.getItemId()) {

                            case R.id.editCourse:
                                Log.d(TAG, "Action edit course @ position: " + courseListVH.getBindingAdapterPosition());
                                Intent intent = new Intent(courseListVH.itemView.getContext(), EditCourseActivity.class);
                                intent.putExtra(Keys.INTENT_COURSECODE, courseCode);
                                intent.putExtra(Keys.INTENT_SECTIONCODE, sectionCode);
                                courseListVH.itemView.getContext().startActivity(intent);
                                return true;

                            case R.id.deleteCourse:
                                ProgressDialog progressDialog =  new ProgressDialog(parent.getContext());
                                Log.d(TAG, "Action delete course @ position: " + courseListVH.getBindingAdapterPosition());
                                new AlertDialog.Builder(courseListVH.itemView.getContext())
                                        .setTitle("Delete course")
                                        .setMessage("Are you sure you want to delete this entry?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressDialog.setMessage("Loading...");
                                                progressDialog.show();
                                                progressDialog.setCanceledOnTouchOutside(false);

                                                //deletes all info from classlist
                                                Db.deleteDocuments(Db.COLLECTION_CLASSLIST,
                                                        Db.FIELD_SECTIONCODE, sectionCode,
                                                        Db.FIELD_COURSECODE, courseCode);
                                                Log.d(TAG,"deleted collection course: " + courseCode + " " + sectionCode);


                                                //deletes all info from course request
                                                Db.deleteDocuments(Db.COLLECTION_COURSEREQUEST,
                                                        Db.FIELD_SECTIONCODE, sectionCode,
                                                        Db.FIELD_COURSECODE, courseCode);

                                                //deletes info from course
                                                Db.deleteDocument(Db.COLLECTION_COURSES,
                                                        Db.FIELD_SECTIONCODE, sectionCode,
                                                        Db.FIELD_COURSECODE, courseCode);
                                                Log.d(TAG,"deleted collection COLLECTION_COURSES");

                                                //deletes all info from meeting history
                                                Db.deleteDocuments(Db.COLLECTION_MEETINGHISTORY,
                                                        Db.FIELD_SECTIONCODE, sectionCode,
                                                        Db.FIELD_COURSECODE, courseCode);
                                                Log.d(TAG,"deleted collection in COLLECTION_MEETINGHISTORY");

                                                //deletes all info from meetings
                                                Db.deleteDocuments(Db.COLLECTION_MEETINGS,
                                                        Db.FIELD_SECTIONCODE, sectionCode,
                                                        Db.FIELD_COURSECODE, courseCode);
                                                Log.d(TAG,"deleted collection in COLLECTION_MEETINGS");

                                                data.remove(courseListVH.getBindingAdapterPosition());

                                                notifyItemRemoved(courseListVH.getBindingAdapterPosition());
                                                notifyItemRangeChanged(courseListVH.getBindingAdapterPosition(), getItemCount() - courseListVH.getBindingAdapterPosition());
                                                Toast.makeText(courseListVH.itemView.getContext(), "Course has been successfully deleted", Toast.LENGTH_SHORT).show();
                                                progressDialog.setCanceledOnTouchOutside(true);
                                                progressDialog.dismiss();
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
                popupMenu.show();
            }

        });
        return courseListVH;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListVH holder, @SuppressLint("RecyclerView") int position) {
        ConstraintLayout courseConstraint = holder.itemView.findViewById(R.id.courseConstraint);

        holder.setTxtClassCode(data.get(position).getCourseCode());
        holder.setTxtSectionCode(data.get(position).getSectionCode());

        if(data.get(position).getIsPublished()) {
            courseConstraint.setBackgroundTintList(
            holder.itemView.getContext().
            getResources().
            getColorStateList(R.color.dark_greenv2));
        } else {
            courseConstraint.setBackgroundTintList(
            holder.itemView.getContext().
            getResources().
            getColorStateList(R.color.gray));
        }

        //When an item is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleClassViewIntent = new Intent(holder.itemView.getContext(), SingleClassActivity.class);
                Log.d(TAG, "Course being passed: " + data.get(position).getCourseCode());
                Log.d(TAG, "Section being passed: " + data.get(position).getSectionCode());
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
