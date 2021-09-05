package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SingleClassAdapter extends RecyclerView.Adapter<SingleClassVH> {
    private static final String TAG = "Single Class Adapter";

    //store data here
    private ArrayList<MeetingModel> data;

    public SingleClassAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter: initialized");

    }

    @NonNull
    @Override
    public SingleClassVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_meeting, parent, false);

        SingleClassVH viewHolder = new SingleClassVH(view);

        int pos = viewHolder.getAdapterPosition();
        //if(data.get(pos).getClassKey() == data.get(pos).

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleClassVH holder, @SuppressLint("RecyclerView") int position) {

        SimpleDateFormat stringDate = new SimpleDateFormat("MMM dd yyyy | E");

        holder.setTxtStudentsPresent(data.get(position).getStudentsPresent());
        holder.setTxtDate(stringDate.format(data.get(position).getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SingleMeetingActivity.class);

                //get the course code somehow
                intent.putExtra(MyKeys.COURSE_CODE_KEY.name(), data.get(position).getCourseCode());
                intent.putExtra(MyKeys.SECTION_CODE_KEY.name(), data.get(position).getSectionCode());
                intent.putExtra(MyKeys.MEETING_CODE_KEY.name(), data.get(position).getMeetingCode());
                intent.putExtra(MyKeys.DATE_KEY.name(), stringDate.format(data.get(position).getDate()));
                intent.putExtra(MyKeys.PRESENT_STUDENTS_KEY.name(), data.get(position).getStudentsPresent());
                intent.putExtra(MyKeys.MEETING_STATUS_KEY.name(), data.get(position).getMeetingStatus());
                Log.d(TAG, "Status: " + data.get(position).getMeetingStatus());
                Log.d(TAG, "Status: " + data.get(position).getCourseCode());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
