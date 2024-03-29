package com.marcelo.animalguide.models.classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.marcelo.animalguide.firebase.ServicesFirebase;

public class BackupSharedPreferences
{
    private String emailUser;
    private String passwordUser;
    private String nameUser;
    private String typeUser;
    private String nameONG;
    private String pathFoto;
    private String provedor;
    private DatabaseReference databaseReference = ServicesFirebase.getFirebaseDatabase();

    public void saveDatabase(String emailUser)
    {
        DatabaseReference userReference = databaseReference
                .child("shared_preferences_login")
                .child(emailUser);

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

    @Exclude
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

    public String getTypeUser()
    {
        return typeUser;
    }

    public void setTypeUser(String typeUser)
    {
        this.typeUser = typeUser;
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

    public String getProvedor()
    {
        return provedor;
    }

    public void setProvedor(String provedor)
    {
        this.provedor = provedor;
    }
}
