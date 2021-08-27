package com.mobdeve.s15.group1.attendancetrackerstudent;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Collection;
import java.util.List;

public class FirestoreReferences {
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
        getUsersCollectionReference().whereEqualTo(FirestoreReferences.COURSECODE_FIELD, stringRef) //this is a test
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
}
