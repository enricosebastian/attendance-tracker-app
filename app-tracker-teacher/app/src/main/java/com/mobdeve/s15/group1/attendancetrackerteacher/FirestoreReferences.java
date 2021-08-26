package com.mobdeve.s15.group1.attendancetrackerteacher;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collection;
import java.util.List;

public class FirestoreReferences {

    private static FirebaseFirestore firebaseFirestoreInstance = null;
    private static StorageReference storageReferenceInstance = null;
    private static CollectionReference usersRef = null;
    private static CollectionReference coursesRef = null;
    private static CollectionReference meetingsRef = null;
    private static DocumentSnapshot userInfo = null;

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
            HANDLEDBY_FIELD     = "handledBy",
        MEETINGS_COLLECTION     = "Meetings";

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

    public static CollectionReference getUsersCollectionReference() {
        if(usersRef == null) {
            usersRef = getFirestoreInstance().collection(USERS_COLLECTION);
        }
        return usersRef;
    }

    public static CollectionReference getMeetingsCollectionReference() {
        if(meetingsRef == null) {
            meetingsRef = getFirestoreInstance().collection(MEETINGS_COLLECTION);
        }
        return meetingsRef;
    }

    public static DocumentReference getDocumentReference(String stringRef) {
        return getFirestoreInstance().document(stringRef);
    }

    public static DocumentSnapshot getSingleUserData(String stringRef) {

        getUsersCollectionReference().whereEqualTo(FirestoreReferences.EMAIL_FIELD, stringRef) //this is a test
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> result = querySnapshot.getDocuments();
                userInfo = result.get(0);
            }
        });

        return userInfo; //returns null as of now
    }

}
