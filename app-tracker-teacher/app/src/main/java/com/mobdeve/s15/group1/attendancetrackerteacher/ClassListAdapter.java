package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// This is an Adapter associated with the ClassListActivity
public class ClassListAdapter extends RecyclerView.Adapter<ClassListVH>{

    private static final String TAG = "ClassListAdapter";
    private ArrayList<ClassListModel> data;

    public ClassListAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter is initialized");
    }

    @NonNull
    @Override
    public ClassListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_classlist_studentscard, parent, false);

        ClassListVH classListVH = new ClassListVH(view);
        return classListVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassListVH holder, int position) {

        holder.setTxtStudentName(data.get(position).getIdNumber()); //lmao it's all id numbers. i have become db dependent
        holder.setTxtIdNo(data.get(position).getIdNumber());
        holder.setImgProfilePic(data.get(position).getIdNumber());
        holder.setPbTotalAttendance(data.get(position).getCourseCode(), data.get(position).getSectionCode(), data.get(position).getIdNumber());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
