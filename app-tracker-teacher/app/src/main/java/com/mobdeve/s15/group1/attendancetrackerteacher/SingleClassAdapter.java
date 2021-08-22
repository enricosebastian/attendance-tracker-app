package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SingleClassAdapter extends RecyclerView.Adapter<SingleClassVH> {
    private static final String TAG = "Class Adapter";

    //store data here
    private ArrayList<Meeting> data;

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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleClassVH holder, int position) {
        holder.setTxtStudentsPresent(data.get(position).getStudentsPresent());
        String dateString = data.get(position).getMonth() +" "+data.get(position).getDayNumber()+", "+data.get(position).getYear()+" | "+data.get(position).getDayName();
        holder.setTxtDate(dateString);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "going to class: "+dateString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
