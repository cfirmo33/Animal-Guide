package com.marcelo.animalguide.models.classes;

import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class UserONG
{
    private String idONG;
    private String typeUser;
    private String name;
    private String email;
    private String password;
    private String nameONG;
    private String provedor;
    private DatabaseReference databaseReference = ServicesFirebase.getFirebaseDatabase();

    public UserONG()
    {

    }

    public void saveDatabase(String node)
    {
        DatabaseReference userReference = databaseReference.child(node).child(getIdONG());

        userReference.setValue(this);
    }

    @Exclude
    private String getIdONG()
    {
        return EncryptionSHA1.encryptionString(idONG);
    }

    public void setIdONG(String idONG)
    {
        this.idONG = idONG;
    }

    public String getTypeUser()
    {
        return typeUser;
    }

    public void setTypeUser(String typeUser)
    {
        this.typeUser = typeUser;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Exclude
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNameONG()
    {
        return nameONG;
    }

    public void setNameONG(String nameONG)
    {
        this.nameONG = nameONG;
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
