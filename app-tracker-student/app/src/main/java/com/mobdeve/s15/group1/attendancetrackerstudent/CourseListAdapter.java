package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// This is an Adapter associated with the CourseListActivity
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
        return courseListVH;
    }


    @Override
    public void onBindViewHolder(@NonNull CourseListVH holder, @SuppressLint("RecyclerView") int position) {

        //sets view holder data
        holder.setTxtClassCode(data.get(position).getCourseCode());
        holder.setTxtSectionCode(data.get(position).getSectionCode());

        //sets constraint layout color, depending if class is published or not
        ConstraintLayout courseConstraint = holder.itemView.findViewById(R.id.courseConstraint);
        Db.getDocumentsWith(Db.COLLECTION_COURSES,
        Db.FIELD_COURSECODE, data.get(position).getCourseCode(),
        Db.FIELD_SECTIONCODE, data.get(position).getSectionCode()).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);

                if(result.get(0).getBoolean(Db.FIELD_ISPUBLISHED)) {
                    courseConstraint.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(R.color.dark_greenv2));
                } else {
                    courseConstraint.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(R.color.gray));
                }

            }
        });


        /*
            The instructions when a Course item is pressed. It checks to see if the course is published first.
            If it is, launch the SingleClassActivity which displays all the classes created in the course.
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if a Course has yet to be published, it should not have any functionalities yet.
                if(courseConstraint.getBackgroundTintList().equals(holder.itemView.getContext().getResources().getColorStateList(R.color.gray))) {
                    Toast.makeText(holder.itemView.getContext(), "Course is still unpublished", Toast.LENGTH_SHORT).show();
                } else {
                    Db.getDocumentsWith(Db.COLLECTION_COURSES,
                    Db.FIELD_COURSECODE, data.get(position).getCourseCode(),
                    Db.FIELD_SECTIONCODE, data.get(position).getSectionCode()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<DocumentSnapshot> result = Db.getDocuments(task);

                            // When a particular Course is clicked, display all the list of Meetings for that class.
                            if(!result.isEmpty()) {
                                Intent singleClassViewIntent = new Intent(holder.itemView.getContext(), SingleClassActivity.class);
                                singleClassViewIntent.putExtra(Keys.INTENT_COURSECODE, result.get(0).getString(Db.FIELD_COURSECODE));
                                singleClassViewIntent.putExtra(Keys.INTENT_SECTIONCODE, result.get(0).getString(Db.FIELD_SECTIONCODE));
                                singleClassViewIntent.putExtra(Keys.INTENT_COURSENAME, result.get(0).getString(Db.FIELD_COURSENAME));

                                holder.itemView.getContext().startActivity(singleClassViewIntent);
                            } else {
                                Toast.makeText(holder.itemView.getContext(), "The course you are trying to access has already been deleted", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}