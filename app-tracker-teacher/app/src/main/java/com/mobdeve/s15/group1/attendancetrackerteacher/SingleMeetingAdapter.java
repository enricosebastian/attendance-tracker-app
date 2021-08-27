package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SingleMeetingAdapter extends RecyclerView.Adapter<SingleMeetingVH> {

    private static final String TAG = "In SingleMeetingAdapter";

    //store data here
    private ArrayList<StudentPresentListModel> data;

    public SingleMeetingAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter: initialized");
    }

    @NonNull
    @Override
    public SingleMeetingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_singlemeeting_studentlist, parent, false);
        SingleMeetingVH singleMeetingVH = new SingleMeetingVH(view);

        return singleMeetingVH;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMeetingVH holder, int position) {
        holder.setSwitchIsPresent(data.get(position).isPresent);
        holder.setTxtStudentName(data.get(position).getStudentAttended(), data.get(position).getStudentAttended());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
