package com.mobdeve.s15.group1.attendancetrackerteacher;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class Db {
    public static String TAG = "Db.java";

    private static FirebaseFirestore firebaseFirestoreInstance = null;
    private static StorageReference storageReferenceInstance = null;
    private static CollectionReference usersRef = null;
    private static CollectionReference meetingCollection = null;
    private static CollectionReference coursesRef = null;
    private static CollectionReference meetingsRef = null;
    private static DocumentSnapshot userInfo = null;
    private static DocumentSnapshot classInfo = null;

    private static List<DocumentSnapshot> resultList = null;

    private static ArrayList<StudentPresentListModel> studentPresentListModels = new ArrayList<>();
    private static ArrayList<ClassModel> classModels = new ArrayList<>();

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

    public static ArrayList<ClassModel> toClassModel(List<DocumentSnapshot> result) {
        classModels.clear();
        for(DocumentSnapshot ds:result) {
            classModels.add(new ClassModel(
                ds.getString("courseCode"),
                ds.getString("courseName"),
                ds.getString("handledBy"),
                ds.getBoolean("isPublished"),
                ds.getString("sectionCode"),
                Integer.parseInt(ds.get("studentCount").toString())));
        }
        return classModels;
    }

    public static String getIdFromTask(Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        String id = qs.getDocuments().get(0).getId();
        return id;
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

    public static void updateSingleCourse(String courseCode, String sectionCode, ClassModel initialInfo) {
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
                    if(initialInfo.getStudentCount() <= 0) initialInfo.setStudentCount(Integer.parseInt(ds.getString("studentCount")));

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

    public static void deleteDocument(String collection, String fieldName, String fieldValue) {
        getFirestoreInstance().collection(collection).whereEqualTo(fieldName,fieldValue).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = getIdFromTask(task);
                getFirestoreInstance().collection(collection).document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"Successfully deleted a "+collection+" document");
                        } else {
                            Log.d(TAG,"Failed: "+task.getException());
                        }
                    }
                });
            }
        });
    }

    public static void deleteDocumentWithTwoParameters(String collection, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2) {
        getFirestoreInstance().collection(collection)
            .whereEqualTo(fieldName1,fieldValue1)
            .whereEqualTo(fieldName2,fieldValue2)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id = getIdFromTask(task);
                Log.d(TAG,"Successfully deleted an "+id+" document");
                getFirestoreInstance().collection(collection).document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"Successfully deleted a "+collection+" document");
                        } else {
                            Log.d(TAG,"Failed: "+task.getException());
                        }
                    }
                });

            }
        });

    }

    public static Task uploadImage(String username, Uri imgUri) {
        return getStorageReferenceInstance().child(username).putFile(imgUri);
    }

    public static Task<Uri> getImageUri(String username) {
        return getStorageReferenceInstance().child(username).getDownloadUrl();
    }

}

