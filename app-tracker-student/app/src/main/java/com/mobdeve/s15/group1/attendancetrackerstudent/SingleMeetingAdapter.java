//package com.mobdeve.s15.group1.attendancetrackerstudent;
//
//import android.annotation.SuppressLint;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//
//public class SingleMeetingAdapter extends RecyclerView.Adapter<SingleMeetingVH> {
//
//    private static final String TAG = "In SingleMeetingAdapter";
//
//    //store data here
//    private ArrayList<StudentPresentListModel> data;
//
//    public SingleMeetingAdapter(ArrayList data) {
//        this.data = data;
//        Log.d(TAG, "Adapter: initialized");
//    }
//
//    @NonNull
//    @Override
//    public SingleMeetingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.layout_singlemeeting_studentlist, parent, false);
//        SingleMeetingVH singleMeetingVH = new SingleMeetingVH(view);
//
//        return singleMeetingVH;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SingleMeetingVH holder, @SuppressLint("RecyclerView") int position) {
//        holder.setBtnIsPresent(data.get(position).isPresent);
//        holder.setTxtStudentName(data.get(position).getFirstName(), data.get(position).getLastName());
//
//        holder.getBtnIsPresent().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
//                        Db.FIELD_MEETINGCODE, data.get(position).getMeetingCode(), //this fucker gets the meeting code (for querying)
//                        Db.FIELD_STUDENTATTENDED, data.get(position).getStudentAttended()). //this fucker gets the student name (for querying)
//                        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //GET 'STUDENTNAME' WITH 'MEETINGCODE', SET 'ISPRESENT'
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        boolean isPresent = holder.getIsPresent();
//                        isPresent = !isPresent; //since user clicked on the button that means the student is no longer present
//                        String documentId = Db.getIdFromTask(task);
//                        Db.getCollection(Db.COLLECTION_MEETINGHISTORY).
//                                document(documentId).
//                                update(Db.FIELD_ISPRESENT, isPresent).
//                                addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        boolean isPresent = holder.getIsPresent();
//                                        isPresent = !isPresent;
//                                        Log.d(TAG, "Successfully changed status isPresent status to "+ isPresent);
//                                        holder.setBtnIsPresent(isPresent);
//                                    }
//                                });
//                    }
//                });
//            }
//        });
//        //change this to your shit if you wanna improve that button you lil shits
////        Switch switchIsPresent = holder.itemView.findViewById(R.id.switchIsPresent);
////        switchIsPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                Db.getDocumentsWith(Db.COLLECTION_MEETINGHISTORY,
////                Db.FIELD_MEETINGCODE, data.get(position).getMeetingCode(), //this fucker gets the meeting code (for querying)
////                Db.FIELD_STUDENTATTENDED, data.get(position).getStudentAttended()). //this fucker gets the student name (for querying)
////                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //GET 'STUDENTNAME' WITH 'MEETINGCODE', SET 'ISPRESENT'
////                    @Override
////                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                        String documentId = Db.getIdFromTask(task);
////                        Db.getCollection(Db.COLLECTION_MEETINGHISTORY).
////                        document(documentId).
////                        update(Db.FIELD_ISPRESENT, b).
////                        addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////                                Log.d(TAG, "Successfully changed status isPresent status to "+b);
////                            }
////                        });
////                    }
////                });
////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//}
