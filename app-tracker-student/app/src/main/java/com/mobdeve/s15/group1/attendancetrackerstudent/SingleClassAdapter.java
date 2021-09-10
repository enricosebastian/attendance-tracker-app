package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SingleClassAdapter extends RecyclerView.Adapter<SingleClassVH> {
    private static final String TAG = "SingleClassAdapter";

    //shared preferences initialization
    private SharedPreferences           sp;
    private String                      email;
    ////////////

    //store data here
    private ArrayList<MeetingModel> data;

    public SingleClassAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Adapter: initialized");

    }

    @NonNull
    @Override
    public SingleClassVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater     = LayoutInflater.from(parent.getContext());
        View view                   = inflater.inflate(R.layout.layout_meeting, parent, false);

        //initializes important user info for db-related functionality
        this.sp                     = parent.getContext().getSharedPreferences(Keys.SP_FILE_NAME, Context.MODE_PRIVATE);
        this.email                  = sp.getString(Keys.SP_EMAIL_KEY,"");

        SingleClassVH viewHolder = new SingleClassVH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleClassVH holder, @SuppressLint("RecyclerView") int position) {

        SimpleDateFormat stringDate = new SimpleDateFormat("MMM dd, yyyy | E");
        holder.setTxtDate(stringDate.format(data.get(position).getMeetingStart()));

        holder.setTxtIsPresent(data.get(position).getMeetingCode(), email);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleMeetingActivityIntent = new Intent(holder.itemView.getContext(), SingleMeetingActivity.class);

                //get the course code somehow
                singleMeetingActivityIntent.putExtra(Keys.INTENT_COURSECODE, data.get(position).getCourseCode());
                singleMeetingActivityIntent.putExtra(Keys.INTENT_SECTIONCODE, data.get(position).getSectionCode());
                singleMeetingActivityIntent.putExtra(Keys.INTENT_MEETINGCODE, data.get(position).getMeetingCode());
                singleMeetingActivityIntent.putExtra(Keys.INTENT_ISOPEN, data.get(position).getIsOpen());

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss"); //for data collection, just in case
                String stringDate = formatter.format(data.get(position).getMeetingStart());
                singleMeetingActivityIntent.putExtra(Keys.INTENT_MEETINGSTART, stringDate);

                holder.itemView.getContext().startActivity(singleMeetingActivityIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}