package com.marcelo.animalguide.models.classes;

import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class UserClass
{
    private String idUser;
    private String typeUser;
    private String name;
    private String email;
    private String password;
    private String provedor;
    private DatabaseReference databaseReference = ServicesFirebase.getFirebaseDatabase();

    public UserClass ()
    {

    }

    public void saveDatabase(String node)
    {
        DatabaseReference userReference = databaseReference.child(node).child(getIdUser());

        userReference.setValue(this);
    }

    @Exclude
    private String getIdUser()
    {
        return EncryptionSHA1.encryptionString(idUser);
    }

    public void setIdUser(String idUser)
    {
        this.idUser = idUser;
    }

    public String getTypeUser ()
    {
        return typeUser;
    }

    public void setTypeUser (String typeUser)
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

    public String getProvedor()
    {
        return provedor;
    }

    public void setProvedor(String provedor)
    {
        this.provedor = provedor;
    }
}
