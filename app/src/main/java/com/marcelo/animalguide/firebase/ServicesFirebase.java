package com.marcelo.animalguide.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * @Author Marcelo Caregnato Cordeiro de Souza
 * Class responsible for communicating with the Firebase server and allowing the use of its resources
 */

public class ServicesFirebase
{
    private static FirebaseAuth firebaseAuth;
    private static StorageReference firebaseStorage;
    private static DatabaseReference databaseReference;

    public static FirebaseAuth getFirebaseAuth()
    {
        if(firebaseAuth == null)
        {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static DatabaseReference getFirebaseDatabase()
    {
        if (databaseReference == null)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static StorageReference getFirebaseStorage()
    {
        if (firebaseStorage == null)
        {
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return firebaseStorage;
    }
}
