package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AcceptStudentsAdapter extends RecyclerView.Adapter<AcceptStudentsVH> {


    private ArrayList<Student> data;

    /**
     * This method creates the view holder for the accept students view
     * @param parent the recycler view
     * @param viewType the viewType
     * */
    @NonNull
    @Override
    public AcceptStudentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_student_requests, parent, false);

        AcceptStudentsVH acceptStudentsVH = new AcceptStudentsVH(view);
        return acceptStudentsVH;
    }

    /**
     * This method binds the data once the view holder has been created
     * @param holder the view holder
     * @param position the point in the model's list
     * */
    @Override
    public void onBindViewHolder(@NonNull AcceptStudentsVH holder, int position) {
        holder.setIvDisplayPicture(data.get(position).getImageId());
        holder.setTvStudentName(data.get(position).getStudentName());
    }

    /**
     * This method returns the size/length of the data
     * @return size/length of the data
     * */
    @Override
    public int getItemCount(){
        return data.size();
    }

}
