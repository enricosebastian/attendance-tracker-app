package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

// This is a View Holder associated with the SingleClassActivity
public class SingleClassVH extends RecyclerView.ViewHolder {
    private static final String     TAG = "SingleClassViewHolder";

    private TextView                txtIsPresent,
                                    txtDate;
    private Drawable                txtIsPresentDrawable;

    public SingleClassVH(@NonNull View itemView) {
        super(itemView);
        this.txtIsPresent           = itemView.findViewById(R.id.txtIsPresent);
        this.txtDate                = itemView.findViewById(R.id.txtDate);
        this.txtIsPresentDrawable   = txtIsPresent.getBackground();
        txtIsPresentDrawable        = DrawableCompat.wrap(txtIsPresentDrawable);
    }

    public void setTxtIsPresent(String meetingCode, String email) {

        //sets if the student that attended was present or absent during class
        Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
        Db.FIELD_MEETINGCODE, meetingCode,
        Db.FIELD_STUDENTATTENDED, email,
        Db.FIELD_ISPRESENT, true).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                if(result.size() == 0) {
                    txtIsPresent.setText("X");
                    DrawableCompat.setTint(txtIsPresentDrawable, itemView.getContext().getResources().getColor(R.color.red_light));

                } else {
                    txtIsPresent.setText("O");

                    Drawable txtDrawable = txtIsPresent.getBackground();
                    DrawableCompat.setTint(txtIsPresentDrawable, itemView.getContext().getResources().getColor(R.color.green));
                }
            }
        });

    }

    public void setTxtDate(String txtDate) {
        this.txtDate.setText(txtDate);
    }

}