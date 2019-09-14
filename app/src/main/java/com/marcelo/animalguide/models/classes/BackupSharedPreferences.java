package com.marcelo.animalguide.models.classes;

import com.google.firebase.database.DatabaseReference;
import com.marcelo.animalguide.firebase.ServicesFirebase;

public class BackupSharedPreferences
{
    private String emailUser;
    private String passwordUser;
    private String nameUser;
    private String nameONG;
    private String pathFoto;
    private DatabaseReference databaseReference = ServicesFirebase.getFirebaseDatabase();

    public void saveDatabase(String data)
    {
        DatabaseReference userReference = databaseReference
                .child("backup_shared_preferences")
                .child(data);

        userReference.setValue(this);
    }

    public String getEmailUser()
    {
        return emailUser;
    }

    public void setEmailUser(String emailUser)
    {
        this.emailUser = emailUser;
    }

    public String getPasswordUser()
    {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser)
    {
        this.passwordUser = passwordUser;
    }

    public String getNameUser()
    {
        return nameUser;
    }

    public void setNameUser(String nameUser)
    {
        this.nameUser = nameUser;
    }

    public String getNameONG()
    {
        return nameONG;
    }

    public void setNameONG(String nameONG)
    {
        this.nameONG = nameONG;
    }

    public String getPathFoto()
    {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto)
    {
        this.pathFoto = pathFoto;
    }
}
