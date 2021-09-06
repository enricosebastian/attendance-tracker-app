package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        return courseListVH;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListVH holder, @SuppressLint("RecyclerView") int position) {
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
