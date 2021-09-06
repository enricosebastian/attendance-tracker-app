package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClassListVH extends RecyclerView.ViewHolder {

    private static final String TAG = "ClassListVH.java";
    private TextView txtStudentName, txtIdNo;
    private ImageView imgProfilePic;
    private ProgressBar pbTotalAttendance;

    public ClassListVH(@NonNull View itemView) {
        super(itemView);
        this.txtStudentName = itemView.findViewById(R.id.txtStudentName);
        this.txtIdNo = itemView.findViewById(R.id.txtIdNo);
        this.imgProfilePic = itemView.findViewById(R.id.imgProfilePic);
        this.pbTotalAttendance = itemView.findViewById(R.id.pbTotalAttendance);
    }

    public void setTxtStudentName(String idNumber) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_IDNUMBER, idNumber,
        Db.FIELD_USERTYPE, "student").
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                if(result.isEmpty()) {
                    Log.d(TAG,"This illogical error happens if someone messes with the Database...");
                } else {
                    txtStudentName.setText(result.get(0).getString(Db.FIELD_FIRSTNAME)+" "+result.get(0).getString(Db.FIELD_LASTNAME));
                }
            }
        });
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

    public void setPbTotalAttendance(String courseCode, String sectionCode, String idNumber) {
        //an example of overdependence on databases lol

        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_IDNUMBER, idNumber).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                String email = result.get(0).getString(Db.FIELD_EMAIL);

                Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
                Db.FIELD_COURSECODE, courseCode,
                Db.FIELD_SECTIONCODE, sectionCode,
                Db.FIELD_STUDENTATTENDED, email).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int totalMeetings = Db.getDocuments(task).size();

                        Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
                        Db.FIELD_STUDENTATTENDED, email,
                        Db.FIELD_COURSECODE, courseCode,
                        Db.FIELD_SECTIONCODE, sectionCode,
                        Db.FIELD_ISPRESENT, true).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                int attendanceCount = Db.getDocuments(task).size();
                                float decimalResult = (float) attendanceCount*100/totalMeetings;
                                Log.d(TAG,"total attendance is only "+attendanceCount+" out of "+totalMeetings+" or "+decimalResult);
                                pbTotalAttendance.setProgress( (int) decimalResult ); //quick maths
                            }
                        });

                    }
                });


            }
        });
    }


}
