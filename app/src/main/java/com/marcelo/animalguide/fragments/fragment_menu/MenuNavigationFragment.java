package com.marcelo.animalguide.fragments.fragment_menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.login.AccountsActivity;
import com.marcelo.animalguide.activitys.login.LoginActivity;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuNavigationFragment extends Fragment
{
    private View view;
    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private DatabaseReference firebaseRef = ServicesFirebase.getFirebaseDatabase();
    private DatabaseReference logoutRef;
    private StorageReference storageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private CircleImageView imageUserMenu;
    private Button btnFriends, btnBlogs, btnEventos, btnPets, btnVideos, btnHelp, btnSettings, btnSobre, btnLogout;
    private TextView txtNameUser, txtOpenProfile;

    private String typeAccount, getNameUser;
    private Uri url;
    private boolean login;

    public MenuNavigationFragment()
    {

    }

    @Override
    public void onStart()
    {
        super.onStart();
        loadingDadosPreferences();
    }

    private void loadingDadosPreferences()
    {
        retrievePhoto();
        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("nome_user_menu"))
        {
            String getNamePreferences = sharedPreferences.getString("nome_user_menu", "");
            txtNameUser.setText(getNamePreferences);
        }
        else
        {
            getDataUser();
        }
    }

    private void getDataUser()
    {
        String id = EncryptionSHA1.encryptionString(userFirebase.getEmail());

        DatabaseReference accountsRef = ServicesFirebase.getFirebaseDatabase()
                .child("registered_users")
                .child(Objects.requireNonNull(id));

        accountsRef.addValueEventListener(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);

                typeAccount = Objects.requireNonNull(userClass).getTypeUser();
                getNameUser = Objects.requireNonNull(userClass).getName();
                url = userFirebase.getPhotoUrl();

                switch (typeAccount)
                {
                    case "Pet Owner":
                    case "Student":
                    case "Veterinary":
                    case "ONG":
                        txtNameUser.setText(getNameUser);
                        retrievePhoto();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                MessagesToast.createMessageError(getString(R.string.exception_accounts_user_database) + databaseError, getActivity());
            }
        });
    }

    private void retrievePhoto()
    {
        String idStorage = EncryptionSHA1.encryptionString(userFirebase.getEmail());

        StorageReference imageReference = storageReference
                .child("Cadastro do Usuário")
                .child(Objects.requireNonNull(idStorage))
                .child("photo.png");

        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                Picasso.get()
                        .load(uri)
                        .into(imageUserMenu);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_menu_navigation, container, false);

        initializeObjects();
        clickButtons();

        return view;
    }

    private void initializeObjects()
    {
        imageUserMenu = view.findViewById(R.id.photoUserMenu);
        btnFriends = view.findViewById(R.id.buttonFriendsMenu);
        btnBlogs = view.findViewById(R.id.buttonBlogsMenu);
        btnEventos = view.findViewById(R.id.buttonEventosMenu);
        btnPets = view.findViewById(R.id.buttonPetsMenu);
        btnVideos = view.findViewById(R.id.buttonVideosMenu);
        btnHelp = view.findViewById(R.id.buttonHelpMenu);
        btnSettings = view.findViewById(R.id.buttonSettingsMenu);
        btnSobre = view.findViewById(R.id.buttonSobreMenu);
        btnLogout = view.findViewById(R.id.buttonLogoutMenu);
        txtNameUser = view.findViewById(R.id.textViewNameUserMenu);
        txtOpenProfile = view.findViewById(R.id.textViewOpenProfileMenu);
    }

    private void clickButtons()
    {
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logoutUser();
            }
        });
    }

    private void logoutUser()
    {
        auth.signOut();
        String idDatabase = EncryptionSHA1.encryptionString(userFirebase.getEmail());
        logoutRef = firebaseRef
                .child("registered_users")
                .child(Objects.requireNonNull(idDatabase));

        logoutRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    UserClass userClass = dataSnapshot.getValue(UserClass.class);

                    login = Objects.requireNonNull(userClass).getSaveLogin();

                    if (login)
                    {
                        Intent intent = new Intent(getActivity(), AccountsActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                MessagesToast.createMessageError(getString(R.string.exception_database_logout)+databaseError, getActivity());
            }
        });
    }
}
