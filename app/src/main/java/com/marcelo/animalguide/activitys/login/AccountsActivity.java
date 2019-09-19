package com.marcelo.animalguide.activitys.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.main_activitys.OwnerMainActivity;
import com.marcelo.animalguide.encryption.Base64Custom;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsActivity extends AppCompatActivity
{
    private Activity activity = this;
    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private StorageReference storageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private ConstraintLayout constraintLayout;
    private CircleImageView imageAccountUser;
    private EditText editTextEmailAccount;
    private TextView textTypeAccount;
    private AlertDialog dialogRelogin;

    private boolean check;

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

            StorageReference imageReference = storageReference.child("Cadastro do Usuário").child(Objects.requireNonNull(idStorage)).child("photo.png");

            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Picasso.get().load(uri).into(imageAccountUser);
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

    private boolean checkConection()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected())
        {
            check = true;
        }
        else
        {
            check = false;
        }

        return check;
    }

    private void initializeObjects()
    {
        constraintLayout = findViewById(R.id.constraintLayoutAccounts);
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

    private void updateAuthPreferences()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains("authenticate_user"))
        {
            editor.putString("authenticate_user", String.valueOf(true));
            editor.apply();
        }
    }

    private void createSnackBar()
    {
        Snackbar.make(constraintLayout, R.string.text_snack_bar_login_check_internet, Snackbar.LENGTH_LONG).show();
    }

    private void createAlertDialogRelogin()
    {
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(activity));
        alertDialogBuilder.setCancelable(false);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.loading, null);

        TextView txtLoading = alertView.findViewById(R.id.textViewTitleLoading);
        txtLoading.setText(getString(R.string.dialog_text_relogin));

        alertDialogBuilder.setView(alertView);
        dialogRelogin = alertDialogBuilder.create();
        dialogRelogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRelogin.show();
    }

    public void reauthenticateUser(View view)
    {
        check = checkConection();

        if (check)
        {
            createAlertDialogRelogin();

            sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

            if (sharedPreferences.contains("email_user") && sharedPreferences.contains("password_criptografado_base64"))
            {
                try
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(sharedPreferences.getString("email_user", ""), Base64Custom.decryption(sharedPreferences.getString("password_criptografado_base64", "")));

                    userFirebase.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            updateAuthPreferences();
                            Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            dialogRelogin.cancel();
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                catch (Exception e)
                {
                    MessagesToast.createMessageError(getString(R.string.exception_relogin) + e, activity);
                }
            }
        }
        else
        {
            dialogRelogin.cancel();
            createSnackBar();
        }
    }

    public void removeAccount(View view)
    {

    }
}