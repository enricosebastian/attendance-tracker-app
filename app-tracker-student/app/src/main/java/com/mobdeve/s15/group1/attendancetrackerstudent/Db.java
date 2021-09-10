package com.mobdeve.s15.group1.attendancetrackerstudent;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Db {
    public static String TAG = "Db.java";

    private static FirebaseFirestore firebaseFirestoreInstance                  = null;
    private static StorageReference storageReferenceInstance                    = null;

    private static ArrayList<CourseModel> courseModels = new ArrayList<>();
    private static ArrayList<MeetingModel> meetingModels                        = new ArrayList<>();
    private static ArrayList<StudentPresentListModel> studentPresentListModels  = new ArrayList<>();

    public static String id = null;

    public final static String
        COLLECTION_USERS        = "Users",
        FIELD_EMAIL             = "email",
        FIELD_FIRSTNAME         = "firstName",
        FIELD_IDNUMBER          = "idNumber",
        FIELD_LASTNAME          = "lastName",
        FIELD_PASSWORD          = "password",
        FIELD_USERTYPE          = "userType",

        COLLECTION_COURSES      = "Courses",
        FIELD_COURSECODE        = "courseCode",
        FIELD_COURSENAME        = "courseName",
        FIELD_HANDLEDBY         = "handledBy",
        FIELD_ISPUBLISHED       = "isPublished",
        FIELD_SECTIONCODE       = "sectionCode",
        FIELD_STUDENTCOUNT      = "studentCount",

        COLLECTION_MEETINGS     = "Meetings",   //has same fields as collection courses
        FIELD_ISOPEN            = "isOpen",
        FIELD_MEETINGCODE       = "meetingCode",
        FIELD_MEETINGSTART      = "meetingStart",

        COLLECTION_MEETINGHISTORY       = "MeetingHistory",   //has same fields as collection courses
        FIELD_ISPRESENT                 = "isPresent",
        FIELD_STUDENTATTENDED           = "studentAttended",

        COLLECTION_COURSEREQUEST    = "CourseRequest",

        COLLECTION_CLASSLIST        = "ClassList";


    public static FirebaseFirestore getFirestoreInstance() {
        if(firebaseFirestoreInstance == null) {
            firebaseFirestoreInstance = FirebaseFirestore.getInstance();
        }
        return firebaseFirestoreInstance;
    }

    public static StorageReference getStorageReferenceInstance() {
        if (storageReferenceInstance == null) {
            storageReferenceInstance = FirebaseStorage.getInstance().getReference();
        }
        return storageReferenceInstance;
    }

    public static CollectionReference getCollection(String collectionName) {
        return getFirestoreInstance().collection(collectionName);
    }

    public static List<DocumentSnapshot> getDocuments(Task<QuerySnapshot> task) {
        return task.getResult().getDocuments();
    }

    //getting original id of a single document
    public static String getIdFromTask(Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if(qs.isEmpty()) {
            id = null;
            Log.d(TAG,"getIdFromTask finds no existing ID");
        } else {
            id = qs.getDocuments().get(0).getId();
            Log.d(TAG,"getIdFromTask found an ID of \""+id+"\"");
        }
        return id;
    }

    //get document methods VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    public static Task<QuerySnapshot> getDocumentsWith(String tableName, String field, String value) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field,value).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
    String field, String value,
    String sortingField, Query.Direction direction) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field,value).
                orderBy(sortingField, direction).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
    String field, String value,
    String sortingField1, Query.Direction direction1,
    String sortingField2, Query.Direction direction2) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field,value).
                orderBy(sortingField1, direction1).
                orderBy(sortingField2, direction2).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName, String field1, String value1, String field2, String value2) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
    String field1, String value1,
    String field2, Boolean value2) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
    String field1, String value1,
    String field2, String value2,
    String field3, String value3) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                whereEqualTo(field3, value3).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
    String field1, String value1,
    String field2, String value2,
    String field3, Boolean value3) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                whereEqualTo(field3, value3).
                get();
    }

    //EXCLUSIVELY ONLY FOR CLASSLISTVH.JAVA
    public static Task<QuerySnapshot> getDocumentsWith(
            String tableName, String field1,
            String value1, String field2,
            String value2, String field3,
            String value3, String field4, Boolean value4) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                whereEqualTo(field3, value3).
                whereEqualTo(field4, value4).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
        String field1, String value1,
        String field2, String value2,
        String sortingField, Query.Direction direction) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                orderBy(sortingField, direction).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName,
    String field1, String value1, Query.Direction direction1,
    String field2, String value2, Query.Direction direction2) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                orderBy(field1, direction1).
                whereEqualTo(field2, value2).
                orderBy(field2, direction2).
                get();
    }
    //get document methods ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    //updating picture from storage
    public static Task uploadImage(String documentId, Uri imgUri) {
        return getStorageReferenceInstance().child(documentId).putFile(imgUri);
    }

    //get profile pic via storage
    public static Task<Uri> getProfilePic(String documentId) {
        return Db.getStorageReferenceInstance().child(documentId).getDownloadUrl();
    }

    //general document adding
    public static void addDocument(String tableName, Map<String, Object> input) {
        Db.getCollection(tableName).
        add(input).
        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Input added succesfully");
            }
        }).
        addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document due to "+ e.getMessage());
            }
        });
    }

    //converting List to CourseModel objects
    public static ArrayList<CourseModel> toCourseModel(List<DocumentSnapshot> result) {
        courseModels.clear();
        for(DocumentSnapshot ds:result) {
            courseModels.add(new CourseModel(
                ds.getString("courseCode"),
                ds.getString("sectionCode")
            ));
        }
        return courseModels;
    }

    //converting List to MeetingModel object
    public static ArrayList<MeetingModel> toMeetingModel(List<DocumentSnapshot> result) {
        meetingModels.clear();
        for(DocumentSnapshot ds:result) {
            meetingModels.add(new MeetingModel(
                ds.getString(Db.FIELD_COURSECODE),
                ds.getBoolean(Db.FIELD_ISOPEN),
                ds.getString(Db.FIELD_MEETINGCODE),
                ds.getTimestamp(Db.FIELD_MEETINGSTART).toDate(), //this is how to convert timestamp to Date
                ds.getString(Db.FIELD_SECTIONCODE),
                Integer.parseInt(ds.get(Db.FIELD_STUDENTCOUNT).toString()))
            );
        }
        return meetingModels;
    }


}

