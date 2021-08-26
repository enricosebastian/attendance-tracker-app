package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

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
//
//    @Override
//    public void onClick(View v) {
//        Log.d(TAG, "onCLick: ");
//        showPopupMenu(v);
//    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.editCourse:
//                Log.d(TAG, "Action edit course ");
//                Intent intent2 = new Intent(vh.itemView.getContext(), EditCourse.class);
//                vh.itemView.getContext().startActivity(intent2);
//                return true;
//            case R.id.deleteCourse:
//                Log.d(TAG, "Action delete course ");
//                return true;
//            default:
//                return false;
//        }
//
//    }
//
//    //  Attribution: https://youtu.be/hKyjb4b19YM
//    private void showPopupMenu(View view)  {
//        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//        popupMenu.inflate(R.menu.course_menu);
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.show();
//    }
}
