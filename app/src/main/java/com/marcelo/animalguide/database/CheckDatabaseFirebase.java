package com.marcelo.animalguide.database;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.marcelo.animalguide.models.classes.UserClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CheckDatabaseFirebase extends AppCompatActivity
{
    private static FirebaseAuth authentication = ServicesFirebase.getFirebaseAuth();
    private static FirebaseUser userFirebase = authentication.getCurrentUser();
    private static DatabaseReference usuarioRef;
    private static ValueEventListener valueEventListenerUser;

    private static String typeUser, encryptionEmail;
    private Activity activity = this;

    public CheckDatabaseFirebase()
    {

    }
    
    public String getTypeAccount()
    {
        try
        {
            if (userFirebase != null)
            {
                encryptionEmail = EncryptionSHA1.encryptionString(userFirebase.getEmail());

                usuarioRef = ServicesFirebase.getFirebaseDatabase()
                        .child("registered_users")
                        .child(encryptionEmail);

                valueEventListenerUser = usuarioRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        UserClass userClass = dataSnapshot.getValue(UserClass.class);

                        typeUser = Objects.requireNonNull(userClass).getTypeUser();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        MessagesToast.createMessageError(getString(R.string.exception_get_type_account), activity);
                    }
                });
            }
        }
        catch(Exception e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_acess_firebase_class_get_type_account)+e, activity);
        }
        
        return typeUser;
    }
}
