package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AcceptStudentsVH extends RecyclerView.ViewHolder {

    private static final String TAG = "AcceptStudentsVH.java";
    private TextView txtStudentName, txtIdNo;
    private Button btnConfirm, btnCancel;
    private ImageView imgProfilePic;


    public AcceptStudentsVH(@NonNull View itemView) {
        super(itemView);
        this.txtStudentName = itemView.findViewById(R.id.txtAcceptStudentName);
        this.txtIdNo = itemView.findViewById(R.id.txtAcceptIdNo);
        this.btnConfirm = itemView.findViewById(R.id.btnConfirm);
        this.btnCancel = itemView.findViewById(R.id.btnDeleteRequest);
        this.imgProfilePic = itemView.findViewById(R.id.imgProfilePic);
    }

    public void setTxtStudentName(String txtStudentName) {
        this.txtStudentName.setText(txtStudentName);
    }

    public void setTxtIdNo(String txtIdNo) {
        this.txtIdNo.setText(txtIdNo);
    }

    public void setImgProfilePic(String idNumber) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_IDNUMBER, idNumber).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String documentId = Db.getIdFromTask(task);

                Db.getProfilePic(documentId).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            Uri imgUri = task.getResult();
                            Picasso.get().load(imgUri).into(imgProfilePic);
                        } else {
                            Log.d(TAG,"No profile image found. Switching to default");
                        }
                    }
                });
            }
        });
    }




}
