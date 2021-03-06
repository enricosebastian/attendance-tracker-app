package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
/*
    This is an Adapter associated with the AcceptStudentsActivity

    The operations that happens in this Adapter are:
        1. Accept student
        2. Reject student
    Database operations are performed to update the list of student requesting
    to join the class and admits the one that are accepted.
 */
public class AcceptStudentsAdapter extends RecyclerView.Adapter<AcceptStudentsVH> {

    private static final String TAG = "AcceptStudentsAdapter";
    private ArrayList<CourseRequestModel> data;

    public AcceptStudentsAdapter(ArrayList data) {
        this.data = data;
        Log.d(TAG, "Accept students adapter is initialized");
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

        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_IDNUMBER, data.get(position).getIdNumber(),
        Db.FIELD_USERTYPE, "student").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);
                holder.setTxtStudentName(result.get(0).getString(Db.FIELD_FIRSTNAME)+" "+result.get(0).getString(Db.FIELD_LASTNAME));
            }
        });

        holder.setTxtIdNo(data.get(position).getIdNumber());
        holder.setImgProfilePic(data.get(position).getIdNumber());

        Button btnConfirm = holder.itemView.findViewById(R.id.btnConfirm);

        // notifies the database that a student is accepted into a class, and finally updates the Adapter
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptStudentRequest(data.get(position).getIdNumber(),
                data.get(position).getCourseCode(),
                data.get(position).getSectionCode());
                data.remove(holder.getBindingAdapterPosition());
                notifyItemRemoved(holder.getBindingAdapterPosition());
                notifyItemRangeChanged(holder.getBindingAdapterPosition(), getItemCount() - holder.getBindingAdapterPosition());
            }
        });
        // notifies the database that a student is not accepted into a class, and finally updates the Adapter
        Button btnCancel = holder.itemView.findViewById(R.id.btnDeleteRequest);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectStudentRequest(
                data.get(position).getIdNumber(),
                data.get(position).getCourseCode(),
                data.get(position).getSectionCode(),
                position);
                notifyItemRemoved(holder.getBindingAdapterPosition());
                notifyItemRangeChanged(holder.getBindingAdapterPosition(), getItemCount() - holder.getBindingAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //If the student is accepted to the course
    protected void acceptStudentRequest(String idNumber, String courseCode, String sectionCode) {
        Db.getDocumentsWith(Db.COLLECTION_USERS,
        Db.FIELD_IDNUMBER, idNumber,
        Db.FIELD_USERTYPE, "student").
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = Db.getDocuments(task);

                Log.d(TAG,"this is accept student request "+  sectionCode);

                Map<String, Object> input = new HashMap<>();

                input.put(Db.FIELD_COURSECODE, courseCode);
                input.put(Db.FIELD_EMAIL, result.get(0).getString(Db.FIELD_EMAIL));
                input.put(Db.FIELD_IDNUMBER, result.get(0).getString(Db.FIELD_IDNUMBER));
                input.put(Db.FIELD_SECTIONCODE, sectionCode);

                Db.addDocument(Db.COLLECTION_CLASSLIST, input);

                //Deletes them from the course request since they've been added
                Db.deleteDocument(Db.COLLECTION_COURSEREQUEST,
                Db.FIELD_COURSECODE, courseCode,
                Db.FIELD_SECTIONCODE, sectionCode,
                Db.FIELD_IDNUMBER, idNumber);

            }
        });
    }

    //If request to join the course is deleted
    protected void rejectStudentRequest(String idNumber, String courseCode, String sectionCode, int position) {

        Db.getDocumentsWith(Db.COLLECTION_COURSEREQUEST,
        Db.FIELD_COURSECODE, courseCode,
        Db.FIELD_SECTIONCODE, sectionCode,
        Db.FIELD_IDNUMBER, idNumber).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = Db.getIdFromTask(task);
                if(id == null) {
                    Log.d(TAG,"Such a document in collections \""+Db.COLLECTION_COURSEREQUEST+"\" does not exist");
                } else {
                    Db.getCollection(Db.COLLECTION_COURSEREQUEST).
                    document(id).
                    delete().
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG,"Successfully deleted a document in \" "+Db.COLLECTION_COURSEREQUEST+"\" collection");
                                Db.getDocumentsWith(Db.COLLECTION_COURSEREQUEST,
                                Db.FIELD_COURSECODE, courseCode,
                                Db.FIELD_SECTIONCODE, sectionCode).
                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List<DocumentSnapshot> result = Db.getDocuments(task);
                                        data.clear(); //clears data set, and builds anew
                                        data.addAll(Db.toCourseRequestModel(result));
                                        Log.d(TAG,""+data.size());
                                        notifyDataSetChanged();

                                    }
                                });
                            } else {
                                Log.d(TAG,"Failed: "+task.getException());
                            }
                        }
                    });
                }
            }
        });
    }
}
