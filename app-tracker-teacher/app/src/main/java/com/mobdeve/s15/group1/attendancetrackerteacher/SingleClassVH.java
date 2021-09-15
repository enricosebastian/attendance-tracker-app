package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

// This is a View Holder associated with the SingleClassActivity
public class SingleClassVH extends RecyclerView.ViewHolder {
    private static final String TAG = "SingleClassViewHolder";
    private TextView txtStudentsPresent, txtDate;

    public SingleClassVH(@NonNull View itemView) {
        super(itemView);
        this.txtStudentsPresent = itemView.findViewById(R.id.txtStudentsPresent);
        this.txtDate = itemView.findViewById(R.id.txtDate);
    }

    public void setTxtStudentsPresent(String meetingCode) {
        Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
        Db.FIELD_MEETINGCODE, meetingCode,
        Db.FIELD_ISPRESENT, true).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                txtStudentsPresent.setText(Integer.toString(result.size()));
            }
        });
    }

    public void setTxtDate(String txtDate) {
        this.txtDate.setText(txtDate);
    }

}