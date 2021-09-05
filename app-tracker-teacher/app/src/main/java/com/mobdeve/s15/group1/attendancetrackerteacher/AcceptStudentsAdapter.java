package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AcceptStudentsAdapter extends RecyclerView.Adapter<AcceptStudentsVH> {

    private static final String TAG = "AcceptStudentsAdapter";
    private ArrayList<CourseRequestModel> data;

    public AcceptStudentsAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter is initialized");
    }

    @NonNull
    @Override
    public AcceptStudentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_course_request, parent, false);

        AcceptStudentsVH acceptStudentsVH = new AcceptStudentsVH(view);
        return acceptStudentsVH;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptStudentsVH holder, int position) {

        holder.setTxtStudentName(data.get(position).getFirstName()+" "+data.get(position).getLastName());
        holder.setTxtIdNo(data.get(position).getIdNumber());
        holder.setImgProfilePic(data.get(position).getIdNumber()); //needs id number to get image

        Button btnConfirm = holder.itemView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "confirming...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
