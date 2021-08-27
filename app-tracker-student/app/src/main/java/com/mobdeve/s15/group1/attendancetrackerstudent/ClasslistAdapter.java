package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClasslistAdapter extends RecyclerView.Adapter<ClasslistVH> {
    private static final String TAG = "Class Adapter";

    //store data here
    private ArrayList<ClassModel> data;

    public ClasslistAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter: initialized");
    }

    @NonNull
    @Override
    public ClasslistVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_class, parent, false);


        ClasslistVH classlistVH = new ClasslistVH(view);
        return classlistVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ClasslistVH holder, int position) {
        holder.setTxtClassCode(data.get(position).getClassCode());
        holder.setTxtSectionCode(data.get(position).getSectionCode());
        //ImageButton btnMoreOptions =  holder.itemView.findViewById(R.id.btnMoreOptions);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SingleClassView.class);

                intent.putExtra("_ID",data.get(position).get_id());
                intent.putExtra("CLASSCODE",data.get(position).getClassCode());
                intent.putExtra("SECTIONCODE",data.get(position).getSectionCode());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
