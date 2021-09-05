package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.setImgProfilePic(data.get(position).getIdNumber()); //needs id number to get image\

        Button btnConfirm = holder.itemView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptStudentRequest(data.get(position).getIdNumber(),
                data.get(position).getCourseCode(),
                data.get(position).getSectionCode());
            }
        });

//        Button btnCancel = holder.itemView.findViewById(R.id.btnCancel);
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "disconfirmed, bitch...", Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected void acceptStudentRequest(String idNumber, String courseCode, String sectionCode) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_IDNUMBER, idNumber,
        Db.FIELD_USERTYPE, "student").
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);

                Log.d(TAG,"this is accept student request "+sectionCode);

                Map<String, Object> input = new HashMap<>();

                input.put(Db.FIELD_COURSECODE, courseCode);
                input.put(Db.FIELD_EMAIL, result.get(0).getString(Db.FIELD_EMAIL));
                input.put(Db.FIELD_IDNUMBER, result.get(0).getString(Db.FIELD_IDNUMBER));
                input.put(Db.FIELD_SECTIONCODE, sectionCode);

                Db.addDocument(Db.COLLECTION_CLASSLIST, input);

                Db.deleteDocument(Db.COLLECTION_COURSEREQUEST,
                Db.FIELD_COURSECODE, courseCode,
                Db.FIELD_SECTIONCODE, sectionCode,
                Db.FIELD_IDNUMBER, idNumber);

            }
        });
    }
}
