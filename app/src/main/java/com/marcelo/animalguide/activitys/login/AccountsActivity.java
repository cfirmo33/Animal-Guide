package com.marcelo.animalguide.activitys.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsActivity extends AppCompatActivity
{
    private Activity activity = this;
    private StorageReference storageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private CircleImageView imageAccountUser;
    private EditText editTextEmailAccount;
    private TextView textTypeAccount;

    @Override
    protected void onStart()
    {
        super.onStart();
        loadingDados();
    }

    private void loadingDados()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("email_user") && sharedPreferences.contains("type_user"))
        {
            editTextEmailAccount.setText(sharedPreferences.getString("email_user", ""));
            String getTypeUser = sharedPreferences.getString("type_user", "");

            switch (getTypeUser)
            {
                case "Pet Owner":
                    textTypeAccount.setText(getString(R.string.owner_pet_text));
                    break;
                case "Student":
                    textTypeAccount.setText(getString(R.string.student_account_text));
                    break;
                case "Veterinary":
                    textTypeAccount.setText(getString(R.string.veterinary_text));
                    break;
                case "ONG":
                    textTypeAccount.setText(getString(R.string.ong_account_text));
                    break;
            }

            String idStorage = EncryptionSHA1.encryptionString(sharedPreferences.getString("email_user", ""));

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
                            .into(imageAccountUser);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        initializeObjects();
        desableObjects();
    }

    private void initializeObjects()
    {
        imageAccountUser = findViewById(R.id.imageViewAccountsUser);
        editTextEmailAccount = findViewById(R.id.editTextEmailAccountsUser);
        textTypeAccount = findViewById(R.id.textViewTypeAccount);
    }

    private void desableObjects()
    {
        editTextEmailAccount.setEnabled(false);
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

    public void reauthenticateUser(View view)
    {

    }

    public void removeAccount(View view)
    {

    }
}
