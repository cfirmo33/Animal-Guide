package com.marcelo.animalguide.models.classes;

import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class UserGoogle
{
    private String idUser;
    private String typeUser;
    private String nameGoogle;
    private String emailGoogle;
    private String nameONG;
    private String provedor;
    private String urlPhoto;
    private String saveAccount;
    private Boolean saveLogin;
    private DatabaseReference databaseReference = ServicesFirebase.getFirebaseDatabase();

    public UserGoogle()
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

    public String getTypeUser()
    {
        return typeUser;
    }

    public void setTypeUser(String typeUser)
    {
        this.typeUser = typeUser;
    }

    public String getNameGoogle()
    {
        return nameGoogle;
    }

    public void setNameGoogle(String nameGoogle)
    {
        this.nameGoogle = nameGoogle;
    }

    public String getEmailGoogle()
    {
        return emailGoogle;
    }

    public void setEmailGoogle(String emailGoogle)
    {
        this.emailGoogle = emailGoogle;
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

    public String getUrlPhoto()
    {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto)
    {
        this.urlPhoto = urlPhoto;
    }

    public String getSaveAccount()
    {
        return saveAccount;
    }

    public void setSaveAccount(String saveAccount)
    {
        this.saveAccount = saveAccount;
    }

    public Boolean getSaveLogin()
    {
        return saveLogin;
    }

    public void setSaveLogin(Boolean saveLogin)
    {
        this.saveLogin = saveLogin;
    }
}
