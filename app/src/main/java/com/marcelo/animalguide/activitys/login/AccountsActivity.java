package com.marcelo.animalguide.activitys.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.adapters.AdapterAccounts;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountsActivity extends AppCompatActivity
{
    private Activity activity = this;
    private Context context;
    private List<UserClass> listUsers;
    private AdapterAccounts adapterAccounts;
    private RecyclerView recyclerViewAccounts;

    private DatabaseReference firebaseRef = ServicesFirebase.getFirebaseDatabase();
    private DatabaseReference accountsRef;
    private FirebaseAuth authentication = ServicesFirebase.getFirebaseAuth();

    private String typeAccount, getEmailBundle;

    @Override
    protected void onStart()
    {
        super.onStart();
        listUsers.clear();
        getDataBundle();
        uploadAccounts(getEmailBundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        initializeObjects();
        configureRecyclerAdapterView();
    }

    private void initializeObjects()
    {
        recyclerViewAccounts = findViewById(R.id.recyclerViewAccounts);
        listUsers = new ArrayList<>();
        adapterAccounts = new AdapterAccounts(listUsers, context, "Pet Owner", getEmailBundle);
    }

    private void getDataBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            getEmailBundle = bundle.getString("getEmailBundle");
        }
    }

    private void configureRecyclerAdapterView()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewAccounts.setLayoutManager(layoutManager);
        recyclerViewAccounts.setHasFixedSize(true);
        recyclerViewAccounts.setAdapter(adapterAccounts);
    }

    private void uploadAccounts(String email)
    {
    String idDatabase= EncryptionSHA1.encryptionString("marcelocaregnatodesouza@gmail.com");
        accountsRef = firebaseRef
                .child("registered_users")
                .child(Objects.requireNonNull(idDatabase));

        accountsRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    listUsers.clear();
                    UserClass userClass = dataSnapshot.getValue(UserClass.class);
                    listUsers.add(userClass);
                }
                adapterAccounts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                MessagesToast.createMessageError(getString(R.string.erro_retriveing_accounts_system)+databaseError, activity);
            }
        });
    }

    public void changeAccount(View view)
    {
        startActivity(new Intent(activity, LoginActivity.class));
        finish();
    }

    public void registerAccount(View view)
    {
        startActivity(new Intent(activity, ChooseActivity.class));
        finish();
    }
}
