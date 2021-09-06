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
    //KEEP THIS VVVVVVVVVVVVVVVVVVVVV
    public static String TAG = "Db.java";
    //KEEP THIS ^^^^^^^^^^^^^^^^^^^^^^



    private static CollectionReference usersRef = null;
    private static CollectionReference meetingCollection = null;
    private static CollectionReference coursesRef = null;
    private static CollectionReference meetingsRef = null;
    private static DocumentSnapshot userInfo = null;
    private static DocumentSnapshot classInfo = null;

    private static List<DocumentSnapshot> resultList = null;


    //KEEP THIS HERE::::::::::::::::::::::::
    //KEEP THIS HERE::::::::::::::::::::::::
    //KEEP THIS HERE::::::::::::::::::::::::
    //KEEP THIS HERE::::::::::::::::::::::::
    //KEEP THIS HERE::::::::::::::::::::::::
    private static FirebaseFirestore firebaseFirestoreInstance                  = null;
    private static StorageReference storageReferenceInstance                    = null;

    private static ArrayList<CourseModel> courseModels = new ArrayList<>();
    private static ArrayList<MeetingModel> meetingModels                        = new ArrayList<>();
    private static ArrayList<CourseRequestModel> courseRequestModels            = new ArrayList<>();
    private static ArrayList<StudentPresentListModel> studentPresentListModels  = new ArrayList<>();
    private static ArrayList<ClassListModel> classListModels                    = new ArrayList<>();

    public static String id = null;
    ////////////////////////////////////KEEP THIS HERE::::::::::::::::::::::::
    ////////////////////////////////////KEEP THIS HERE::::::::::::::::::::::::
    ////////////////////////////////////KEEP THIS HERE::::::::::::::::::::::::
    ////////////////////////////////////KEEP THIS HERE::::::::::::::::::::::::
    ////////////////////////////////////KEEP THIS HERE::::::::::::::::::::::::
    ////////////////////////////////////KEEP THIS HERE::::::::::::::::::::::::



    //DELETE SOON:::::::::::::::::::::::::::::::::::
    //DELETE SOON:::::::::::::::::::::::::::::::::::
    //DELETE SOON:::::::::::::::::::::::::::::::::::
    //DELETE SOON:::::::::::::::::::::::::::::::::::
    //DELETE SOON:::::::::::::::::::::::::::::::::::
    //DELETE SOON:::::::::::::::::::::::::::::::::::
    public final static String
        USERS_COLLECTION        = "Users",
            USERNAME_FIELD      = "username",
            IDNUMBER_FIELD      = "idNumber",
            EMAIL_FIELD         = "email",
            PASSWORD_FIELD      = "password",
            FIRSTNAME_FIELD     = "firstName",
            LASTNAME_FIELD      = "lastName",
            USERTYPE_FIELD      = "userType",
        COURSES_COLLECTION      = "Courses",
            COURSECODE_FIELD    = "courseCode",
            COURSENAME_FIELD    = "courseName",
            SECTIONCODE_FIELD   = "sectionCode",
            STUDENTCOUNT_FIELD  = "studentCount",
            ISPUBLISHED_FIELD   = "isPublished",
            HANDLEDBY_FIELD         = "handledBy",
        MEETINGS_COLLECTION         = "Meetings",
            MEETINGCODE_FIELD         = "meetingCode",
        MEETINGHISTORY_COLLECTION       = "MeetingHistory";
    ////////////DELETE SOON////////////////////////////////////////
    ////////////DELETE SOON////////////////////////////////////////
    ////////////DELETE SOON////////////////////////////////////////
    ////////////DELETE SOON////////////////////////////////////////
    ////////////DELETE SOON////////////////////////////////////////
    ////////////DELETE SOON////////////////////////////////////////
    ////////////DELETE SOON////////////////////////////////////////



    //new version
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

        COLLECTION_CLASSLIST        = "ClassList"



        ; //no need for username here

    //FIELD_MEETINGSTATUS     = "meetingStatus", //<-------------- lmao useless delete this later


    ////////////////////////new version



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

    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////NEW AS OF 2021, 09, 04
    public static Task<QuerySnapshot> getTable(String tableName) {
        return getFirestoreInstance().collection(tableName).get();
    }

    public static CollectionReference getCollection(String collectionName) {
        return getFirestoreInstance().collection(collectionName);
    }

    public static List<DocumentSnapshot> getDocuments(Task<QuerySnapshot> task) {
        return task.getResult().getDocuments();
    }

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

    public static Task<QuerySnapshot> getDocumentsWithout(String tableName, String field, String value) {
        return  getFirestoreInstance().
                collection(tableName).
                whereNotEqualTo(field,value).
                get();
    }

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

    public static Task uploadImage(String documentId, Uri imgUri) {
        return getStorageReferenceInstance().child(documentId).putFile(imgUri);
    }

    public static Task<Uri> getProfilePic(String documentId) {
        return Db.getStorageReferenceInstance().child(documentId).getDownloadUrl();
    }

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


    ////////////////////NEW AS OF 2021, 09, 04/////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //gets the entire user collection
    public static CollectionReference getUsersCollectionReference() {
        if(usersRef == null) {
            usersRef = getFirestoreInstance().collection(USERS_COLLECTION);
        }
        return usersRef;
    }

    //gets the entire MeetingHistory collection
    public static CollectionReference getMeetingHistoryCollection() {
        if(meetingCollection == null) {
            meetingCollection = getFirestoreInstance().collection(MEETINGHISTORY_COLLECTION);
        }
        return meetingCollection;
    }

    //gets the entire course collection
    public static CollectionReference getCoursesCollectionReference() {
        if(coursesRef == null) {
            coursesRef = getFirestoreInstance().collection(COURSES_COLLECTION);
        }
        return coursesRef;
    }

    //gets the entire meeting collection
    public static CollectionReference getMeetingsCollectionReference() {
        if(meetingsRef == null) {
            meetingsRef = getFirestoreInstance().collection(MEETINGS_COLLECTION);
        }
        return meetingsRef;
    }

    public static DocumentReference getDocumentReference(String stringRef) {
        return getFirestoreInstance().document(stringRef);
    }

    //returns null as of now
    //searches for a single user
    public static Task<QuerySnapshot> getUserInfoFromEmail(String emailRef) {
        return getUsersCollectionReference().whereEqualTo(EMAIL_FIELD, emailRef).get();
    }

    //returns null as of now
    //searches for a single class
    public static DocumentSnapshot getSingleClassData(String stringRef) {
        getUsersCollectionReference().whereEqualTo(Db.COURSECODE_FIELD, stringRef) //this is a test
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> result = querySnapshot.getDocuments();
                classInfo = result.get(0);
            }
        });
        return classInfo; //returns null as of now
    }

    public static DocumentSnapshot getFirstResult(Task<QuerySnapshot> task) {
        if(userInfo == null) {
            QuerySnapshot querySnapshot = task.getResult();
            List<DocumentSnapshot> result = querySnapshot.getDocuments();
            userInfo = result.get(0);
        }
        return userInfo;
    }

    public static Task<QuerySnapshot> getStudentsInMeeting(String meetingCode) {
        return getMeetingHistoryCollection().whereEqualTo(MEETINGCODE_FIELD, meetingCode).get();
    }

    public static List<DocumentSnapshot> toList(Task<QuerySnapshot> task) {
        QuerySnapshot querySnapshot = task.getResult();
        List<DocumentSnapshot> result = querySnapshot.getDocuments();
        resultList = result;
        return resultList;
    }

    public static ArrayList<StudentPresentListModel> toStudentPresentListModel(List<DocumentSnapshot> result) {
        studentPresentListModels.clear();
        for(DocumentSnapshot ds:result) {
            studentPresentListModels.add(new StudentPresentListModel(
                    ds.getString("courseCode"),
                    ds.getString("meetingCode"),
                    ds.getString("sectionCode"),
                    ds.getString("studentAttended"),
                    ds.getString("firstName"),
                    ds.getString("lastName"),
                    ds.getBoolean("isPresent")));
        }
        return studentPresentListModels;
    }

    public static void updateSingleStudent(String entry, String query, UserModel initialInfo) {
        Db.getUsersCollectionReference()
            .whereEqualTo(entry, query)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    DocumentSnapshot ds = getFirstResult(task);
                    if(initialInfo.getEmail().equals("")) initialInfo.setEmail(ds.getString("email"));
                    if(initialInfo.getPassword().equals("")) initialInfo.setPassword(ds.getString("password"));
                    if(initialInfo.getFirstName().equals("")) initialInfo.setFirstName(ds.getString("firstName"));
                    if(initialInfo.getLastName().equals("")) initialInfo.setLastName(ds.getString("lastName"));
                    if(initialInfo.getIdNumber().equals("")) initialInfo.setIdNumber(ds.getString("idNumber"));
                    if(initialInfo.getUserType().equals("")) initialInfo.setUserType(ds.getString("userType"));


                    String id = Db.getIdFromTask(task);
                    Db.getUsersCollectionReference()
                        .document(id)
                        .set(initialInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("main","Single student document updated successfully.");
                            }
                        });
                }
            });
    }

    public static void updateSingleCourse(String courseCode, String sectionCode, CourseModel initialInfo) {
        Db.getCoursesCollectionReference()
                .whereEqualTo(COURSECODE_FIELD, courseCode)
                .whereEqualTo(SECTIONCODE_FIELD,sectionCode)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    DocumentSnapshot ds = getFirstResult(task);
                    if(initialInfo.getCourseCode().equals("")) initialInfo.setCourseCode(ds.getString("courseCode"));
                    if(initialInfo.getCourseName().equals("")) initialInfo.setCourseName(ds.getString("courseName"));
                    if(initialInfo.getHandledBy().equals("")) initialInfo.setHandledBy(ds.getString("handledBy"));
                    if(initialInfo.getSectionCode().equals("")) initialInfo.setSectionCode(ds.getString("sectionCode"));

                    String id = Db.getIdFromTask(task);
                    Log.d("main","id is "+id);
                    Db.getCoursesCollectionReference()
                            .document(id)
                            .set(initialInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("main","Single course document updated successfully.");
                                }
                            });
                }
            });
    }

    public static Query findDocuments(String collection, String fieldName, String fieldValue) {
        return getFirestoreInstance()
                .collection(collection)
                .whereEqualTo(fieldName,fieldValue);
    }

    public static Query findDocumentsWithTwoParameters(String collection, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2) {
        return getFirestoreInstance()
            .collection(collection)
            .whereEqualTo(fieldName1,fieldValue1)
            .whereEqualTo(fieldName2, fieldValue2);
    }

    public static Task<Uri> getImageUri(String username) {
        return getStorageReferenceInstance().child(username).getDownloadUrl();
    }

}

