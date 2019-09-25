package com.marcelo.animalguide.models.classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.marcelo.animalguide.firebase.ServicesFirebase;

import lombok.Getter;
import lombok.Setter;

public class UserClass
{
    @Exclude @Getter @Setter
    private String idUser;

    @Getter @Setter
    private String typeUser;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String email;

    @Exclude @Getter @Setter
    private String password;

    @Getter @Setter
    private String nameONG;

    @Getter @Setter
    private String provedor;

    @Getter @Setter
    private Boolean saveLogin;

    private DatabaseReference databaseReference = ServicesFirebase.getFirebaseDatabase();

    public UserClass ()
    {

    }

    public void saveDatabase(String node)
    {
        DatabaseReference userReference = databaseReference
                .child(node)
                .child(getIdUser());

        userReference.setValue(this);
    }
}
