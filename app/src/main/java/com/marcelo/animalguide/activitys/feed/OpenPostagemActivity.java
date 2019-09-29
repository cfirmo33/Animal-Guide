package com.marcelo.animalguide.activitys.feed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpenPostagemActivity extends AppCompatActivity
{
    private Activity activity = this;
    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private StorageReference storageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private EditText editTextConteudoPostagem;
    private TextView textNameUserPostagem, textPrivacidadePostagem;
    private CircleImageView imageUserPostagem;
    private RecyclerView recyclerViewItensPostagem;
    private Toolbar toolbar;
    private AlertDialog dialogPrivacidade;

    private String typeAccount, getNameUser;
    private Uri url;

    @Override
    public void onStart()
    {
        super.onStart();
        loadingDadosPreferences();
    }

    private void loadingDadosPreferences()
    {
        retrievePhoto();
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("nome_user_menu"))
        {
            String getNamePreferences = sharedPreferences.getString("nome_user_menu", "");
            textNameUserPostagem.setText(getNamePreferences);
        }
        else
        {
            getDataUser();
        }
    }

    private void getDataUser()
    {
        String id = EncryptionSHA1.encryptionString(userFirebase.getEmail());

        DatabaseReference accountsRef = ServicesFirebase
                .getFirebaseDatabase()
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
                        textNameUserPostagem.setText(getNameUser);
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
                        .into(imageUserPostagem);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_postagem);

        initializedObjects();
    }

    private void initializedObjects()
    {
        editTextConteudoPostagem = findViewById(R.id.editTextTextoPostagem);
        textNameUserPostagem = findViewById(R.id.textViewNameUserOpenPostagens);
        textPrivacidadePostagem = findViewById(R.id.textViewPrivacidadePostagem);
        imageUserPostagem = findViewById(R.id.imageViewOpenPostagens);
        recyclerViewItensPostagem = findViewById(R.id.recyclerViewRecursosPostagem);

        //Settings Toolbar
        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void creatAlertPrivacidade()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_privacidade_posts, null);
        TextView textPublic = alertView.findViewById(R.id.textViewPublic);
        TextView textFriends = alertView.findViewById(R.id.textViewFriends);

        textPublic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textPrivacidadePostagem.setText(getString(R.string.text_public_privacidade_postagens));
                dialogPrivacidade.cancel();
            }
        });

        textFriends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textPrivacidadePostagem.setText(getString(R.string.text_privacidade_friends_postagens));
                dialogPrivacidade.cancel();
            }
        });

        alertDialogBuilder.setView(alertView);
        dialogPrivacidade = alertDialogBuilder.create();
        dialogPrivacidade.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrivacidade.show();
    }

    public void showAlertPrivacidade(View view)
    {
        creatAlertPrivacidade();
    }

    public void publicarPost(View view)
    {

    }

    public void openCameraPostagem(View view)
    {
    }

    public void openVideoPostagem(View view)
    {
    }

    public void openGaleriaPostagem(View view)
    {
    }
}
