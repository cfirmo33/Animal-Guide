package com.marcelo.animalguide.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserFirebase
{

    private static FirebaseUser getUserAtual()
    {
        FirebaseAuth user = ServicesFirebase.getFirebaseAuth();

        return user.getCurrentUser();
    }

    public static void updatePhotoUser(Uri url)
    {
        FirebaseUser user = getUserAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setPhotoUri(url)
                .build();

        try
        {
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (!task.isSuccessful())
                    {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void updateNameUser(String name)
    {
        FirebaseUser user = getUserAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        try
        {
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (!task.isSuccessful())
                    {
                        Log.d("Perfil", "Erro ao atualizar nome do perfil.");
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

