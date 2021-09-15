package com.mobdeve.s15.group1.attendancetrackerteacher;

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
    private static ArrayList<CourseRequestModel> courseRequestModels            = new ArrayList<>();
    private static ArrayList<StudentPresentListModel> studentPresentListModels  = new ArrayList<>();
    private static ArrayList<ClassListModel> classListModels                    = new ArrayList<>();

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

    public static Task<QuerySnapshot> getTable(String tableName) {
        return getFirestoreInstance().collection(tableName).get();
    }

    public static CollectionReference getCollection(String collectionName) {
        return getFirestoreInstance().collection(collectionName);
    }

    public static List<DocumentSnapshot> getDocuments(Task<QuerySnapshot> task) {
        return task.getResult().getDocuments();
    }
    /*
        START OF: Get Document Methods. Multiple arguments can allow the application
        to make queries with varying arguments such as field arguments and orderby.
     */
    public static Task<QuerySnapshot> getDocumentsWith(String tableName, String field, String value) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field,value).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName, String field1, String value1, String field2, String value2) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(String tableName, String field1, String value1, String field2, Boolean value2) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(
            String tableName, String field1,
            String value1, String field2,
            String value2, String field3,
            String value3) {
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

    public static Task<QuerySnapshot> getDocumentsWith(
        String tableName, String field1,
        String value1, String field2,
        String value2, String sortingField,
        Query.Direction direction) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
                whereEqualTo(field2, value2).
                orderBy(sortingField, direction).
                get();
    }

    public static Task<QuerySnapshot> getDocumentsWith(
            String tableName, String field1,
            String value1, String sortingField,
            Query.Direction direction) {
        return  getFirestoreInstance().
                collection(tableName).
                whereEqualTo(field1, value1).
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

    public static Task<QuerySnapshot> getDocumentsWithout(String tableName, String field, String value) {
        return  getFirestoreInstance().
                collection(tableName).
                whereNotEqualTo(field,value).
                get();
    }
    // END OF: Get Document Methods

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

    //updating picture from storage
    public static Task uploadImage(String documentId, Uri imgUri) {
        return getStorageReferenceInstance().child(documentId).putFile(imgUri);
    }

    //get profile pic via storage
    public static Task<Uri> getProfilePic(String documentId) {
        Log.d(TAG, "What it returns: " + Db.getStorageReferenceInstance().child(documentId).getDownloadUrl());
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

    //converting List to ClassModel objects
    public static ArrayList<CourseModel> toClassModel(List<DocumentSnapshot> result) {
        courseModels.clear();
        for(DocumentSnapshot ds:result) {
            courseModels.add(new CourseModel(
                ds.getString("courseCode"),
                ds.getString("courseName"),
                ds.getString("handledBy"),
                ds.getBoolean("isPublished"),
                ds.getString("sectionCode")
            ));
        }
        return courseModels;
    }

    //converting List to CourseRequest objects
    public static ArrayList<CourseRequestModel> toCourseRequestModel(List<DocumentSnapshot> result) {
        courseRequestModels.clear();
        for(DocumentSnapshot ds:result) {
            courseRequestModels.add(new CourseRequestModel(
                    ds.getString(Db.FIELD_COURSECODE),
                    ds.getString(Db.FIELD_FIRSTNAME),
                    ds.getString(Db.FIELD_IDNUMBER),
                    ds.getString(FIELD_LASTNAME),
                    ds.getString(FIELD_SECTIONCODE)
            ));
        }
        return courseRequestModels;
    }

    //converting List to ClassListModel objects
    public static ArrayList<ClassListModel> toClassListModel(List<DocumentSnapshot> result) {
        classListModels.clear();
        for(DocumentSnapshot ds:result) {
            classListModels.add(new ClassListModel(
                    ds.getString(Db.FIELD_COURSECODE),
                    ds.getString(Db.FIELD_EMAIL),
                    ds.getString(Db.FIELD_IDNUMBER),
                    ds.getString(FIELD_SECTIONCODE)
            ));
        }
        return classListModels;
    }

    //converting List to MeetingModel objects
    public static ArrayList<MeetingModel> toMeetingModel(List<DocumentSnapshot> result) {
        meetingModels.clear();
        for(DocumentSnapshot ds:result) {
            //(String courseCode, boolean isOpen, String meetingCode, Date meetingStart, String sectionCode, int studentCount)
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

    //converting List to StudentPresentListModel objects
    public static ArrayList<StudentPresentListModel> toStudentPresentListModel(List<DocumentSnapshot> result) {
        studentPresentListModels.clear();
        for(DocumentSnapshot ds:result) {
            studentPresentListModels.add(new StudentPresentListModel(
                    ds.getString(FIELD_COURSECODE),
                    ds.getString(FIELD_MEETINGCODE),
                    ds.getString(FIELD_SECTIONCODE),
                    ds.getString(FIELD_STUDENTATTENDED),
                    ds.getString(FIELD_FIRSTNAME),
                    ds.getString(FIELD_LASTNAME),
                    ds.getBoolean(FIELD_ISPRESENT)));
        }
        return studentPresentListModels;
    }
    // START OF: Delete Document Methods
    public static void deleteDocument(String tableName, String field, String value) {
        getDocumentsWith(tableName, field, value).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = Db.getIdFromTask(task);
                Db.getCollection(tableName).
                document(id).
                delete().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"Successfully deleted a "+tableName+" document");
                        } else {
                            Log.d(TAG,"Failed: "+task.getException());
                        }
                    }
                });
            }
        });
    }

    public static void deleteDocument(String tableName,
        String field1, String value1,
        String field2, String value2) {
        getDocumentsWith(tableName, field1, value1, field2, value2).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = Db.getIdFromTask(task);
                Log.d(TAG,"Deleting document with an id of \""+id+"\"");
                if(id == null) {
                    Log.d(TAG,"Such a document in collections \""+tableName+"\" does not exist");
                } else {
                    Db.getCollection(tableName).
                    document(id).
                    delete().
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG,"Successfully deleted a document in \" "+tableName+"\" collection");
                            } else {
                                Log.d(TAG,"Failed: "+task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    public static void deleteDocument(String tableName,
        String field1, String value1,
        String field2, String value2,
        String field3, String value3) {
        getDocumentsWith(tableName, field1, value1, field2, value2, field3, value3).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = Db.getIdFromTask(task);
                Log.d(TAG,"Deleting document with an id of \""+id+"\"");
                if(id == null) {
                    Log.d(TAG,"Such a document in collections \""+tableName+"\" does not exist");
                } else {
                    Db.getCollection(tableName).
                    document(id).
                    delete().
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG,"Successfully deleted a document in \" "+tableName+"\" collection");
                            } else {
                                Log.d(TAG,"Failed: "+task.getException());
                            }
                        }
                    });
                }
            }
        });
    }


    public static void deleteDocuments(String tableName,
    String field, String value) {
        getDocumentsWith(tableName, field, value).
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = getDocuments(task);

                for(DocumentSnapshot ds:result) {
                    Db.getCollection(tableName).
                    document(ds.getId()).
                    delete().
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "Deleted "+ds.getId());
                            } else {
                                Log.d(TAG,"Failed: "+task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    public static void deleteDocuments(String tableName,
    String field1, String value1,
    String field2, String value2) {
        getDocumentsWith(tableName, field1, value1, field2, value2).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> result = getDocuments(task);

                for(DocumentSnapshot ds:result) {

                    Db.getCollection(tableName).
                    document(ds.getId()).
                    delete().
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "Deleted "+ds.getId());
                            } else {
                                Log.d(TAG,"Failed: "+task.getException());
                            }
                        }
                    });
                }
            }
        });
    }
    // END OF: Delete Document Methods



}

