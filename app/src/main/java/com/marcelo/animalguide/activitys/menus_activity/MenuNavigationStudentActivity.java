package com.marcelo.animalguide.activitys.menus_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.google_maps_activity.GoogleMapsStudentActivity;
import com.marcelo.animalguide.activitys.login.AccountsActivity;
import com.marcelo.animalguide.activitys.login.LoginActivity;
import com.marcelo.animalguide.activitys.main_activitys.StudentMainActivity;
import com.marcelo.animalguide.activitys.notifications_activity.NotificationsStudentActivity;
import com.marcelo.animalguide.activitys.searchs_activity.SearchStudentActivity;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuNavigationStudentActivity extends AppCompatActivity
{
    private Activity activity = this;
    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private DatabaseReference firebaseRef = ServicesFirebase.getFirebaseDatabase();
    private DatabaseReference logoutRef, userRef;
    private StorageReference storageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private CircleImageView imageUserMenu;
    private Button btnFriends, btnBlogs, btnEventos, btnPets, btnVideos, btnHelp, btnSettings, btnSobre, btnLogout;
    private TextView txtNameUser, txtOpenProfile;
    private AlertDialog dialogLogout, dialogSave;

    private String typeAccount, getNameUser;
    private Uri url;
    private boolean login;

    @Override
    public void onStart()
    {
        super.onStart();
        loadingDadosPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation_student);

        initializeObjects();
        settingsNavigationBottom();
    }

    private void initializeObjects()
    {
        imageUserMenu = findViewById(R.id.photoUserMenu);
        btnFriends = findViewById(R.id.buttonFriendsMenu);
        btnBlogs = findViewById(R.id.buttonBlogsMenu);
        btnEventos = findViewById(R.id.buttonEventosMenu);
        btnPets = findViewById(R.id.buttonPetsMenu);
        btnVideos = findViewById(R.id.buttonVideosMenu);
        btnHelp = findViewById(R.id.buttonHelpMenu);
        btnSettings = findViewById(R.id.buttonSettingsMenu);
        btnSobre = findViewById(R.id.buttonSobreMenu);
        btnLogout = findViewById(R.id.buttonLogoutMenu);
        txtNameUser = findViewById(R.id.textViewNameUserMenu);
        txtOpenProfile = findViewById(R.id.textViewOpenProfileMenu);
    }

    private void settingsNavigationBottom()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.navigation_bottom_menu_student);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30);

        loadingNavigations(bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
    }

    private void loadingNavigations(BottomNavigationViewEx viewEx)
    {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        startActivity(new Intent(activity, StudentMainActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_search:
                        startActivity(new Intent(activity, SearchStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_notification:
                        startActivity(new Intent(activity, NotificationsStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_mapa:
                        startActivity(new Intent(activity, GoogleMapsStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_menu:
                        startActivity(new Intent(activity, MenuNavigationStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }

    private void loadingDadosPreferences()
    {
        retrievePhoto();
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

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
                MessagesToast.createMessageError(getString(R.string.exception_accounts_user_database) + databaseError, activity);
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

    private void createAlertDialogLogout()
    {
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(activity));
        alertDialogBuilder.setCancelable(false);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.loading, null);

        TextView txtLoading = alertView.findViewById(R.id.textViewTitleLoading);
        txtLoading.setText(getString(R.string.dialog_text_exit));

        alertDialogBuilder.setView(alertView);
        dialogLogout = alertDialogBuilder.create();
        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.show();
    }

    private void updateAuthPreferences()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains("authenticate_user"))
        {
            editor.putString("authenticate_user", String.valueOf(false));
            editor.apply();
        }
    }

    private void updateStatusDatabase()
    {
        String idDatabase = EncryptionSHA1.encryptionString(userFirebase.getEmail());

        try
        {
            userRef = firebaseRef.child("registered_users").child(Objects.requireNonNull(idDatabase));

            userRef.child("saveLogin").setValue(true);
        }
        catch (Exception e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_update_status_dados_login) + e, activity);
        }
    }

    private void createDialogSaveLogin()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_title_save_password_login));
        builder.setMessage(getString(R.string.text_aviso_save_dados_login));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.btn_save_password), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                updateStatusDatabase();
                Intent intent = new Intent(activity, AccountsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogLogout.cancel();
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.btn_not_save_password), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                auth.signOut();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        });

        dialogSave = builder.create();
        dialogSave.show();
    }

    private void logout()
    {
        if (userFirebase != null)
        {
            createAlertDialogLogout();

            String idDatabase = EncryptionSHA1.encryptionString(userFirebase.getEmail());
            logoutRef = firebaseRef.child("registered_users").child(Objects.requireNonNull(idDatabase));

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
                            updateAuthPreferences();
                            Intent intent = new Intent(activity, AccountsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            dialogLogout.cancel();
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            createDialogSaveLogin();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    dialogLogout.cancel();
                    MessagesToast.createMessageError(getString(R.string.exception_database_logout) + databaseError, activity);
                }
            });

        }
    }

    public void logoutUser(View view)
    {
        logout();
    }
}
