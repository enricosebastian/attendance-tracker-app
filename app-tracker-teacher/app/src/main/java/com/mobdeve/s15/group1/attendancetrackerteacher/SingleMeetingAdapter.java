package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(@NonNull SingleMeetingVH holder, @SuppressLint("RecyclerView") int position) {
        holder.setBtnIsPresent(data.get(position).isPresent);

        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_EMAIL, data.get(position).
        getStudentAttended()).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                holder.setTxtStudentName(result.get(0).getString(Db.FIELD_FIRSTNAME), result.get(0).getString(Db.FIELD_LASTNAME));
            }
        });

        holder.getBtnIsPresent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
                        Db.FIELD_MEETINGCODE, data.get(position).getMeetingCode(),
                        Db.FIELD_STUDENTATTENDED, data.get(position).getStudentAttended()).
                        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //GET 'STUDENTNAME' WITH 'MEETINGCODE', SET 'ISPRESENT'
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isPresent = holder.getIsPresent();
                        isPresent = !isPresent; //since user clicked on the button that means the student is no longer present
                        String documentId = Db.getIdFromTask(task);
                        Db.getCollection(Db.COLLECTION_MEETINGHISTORY).
                                document(documentId).
                                update(Db.FIELD_ISPRESENT, isPresent).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        boolean isPresent = holder.getIsPresent();
                                        isPresent = !isPresent;
                                        Log.d(TAG, "Successfully changed status isPresent status to "+ isPresent);
                                        holder.setBtnIsPresent(isPresent);
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
