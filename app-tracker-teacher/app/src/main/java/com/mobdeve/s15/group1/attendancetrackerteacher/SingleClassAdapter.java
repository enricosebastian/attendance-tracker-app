package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SingleClassAdapter extends RecyclerView.Adapter<SingleClassVH> {
    private static final String TAG = "Class Adapter";

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
                Intent intent = new Intent(holder.itemView.getContext(), SingleMeetingView.class);
                intent.putExtra("DATE_KEY", stringDate.format(data.get(position).getDate()));
                intent.putExtra("STUDENTSPRESENT_KEY", data.get(position).getStudentsPresent());

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
