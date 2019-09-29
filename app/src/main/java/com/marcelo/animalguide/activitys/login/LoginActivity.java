package com.marcelo.animalguide.activitys.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.main_activitys.FeedActivity;
import com.marcelo.animalguide.activitys.password.ForgotPasswordActivity;
import com.marcelo.animalguide.activitys.splash_screen.SplashScreenActivity;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private static final int GOOGLE_SIGN = 300;
    private Activity activity = this;
    private String exception, nameGoogle, emailGoogle, getTypeAccount, typeAccount;
    private Boolean check;

    private EditText editTextEmail, editTextPassword;
    private AlertDialog dialogLoading;
    private ConstraintLayout constraintLayout;

    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private GoogleApiClient googleApiClient;
    private GoogleSignInResult result;
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    @Override
    protected void onStart()
    {
        super.onStart();
        checkConection();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();
    }

    private boolean checkConection()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean getStatusInternet;

        if (netInfo != null && netInfo.isConnected())
        {
            getStatusInternet = true;
            checkAuthentication();
        }
        else
        {
            getStatusInternet = false;
            loginOffline();
        }

        return getStatusInternet;
    }

    private void openActivitySplash()
    {
        startActivity(new Intent(activity, SplashScreenActivity.class));
        finish();
    }

    private void loginOffline()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("authenticate_user"))
        {
            boolean authUser = Boolean.parseBoolean(sharedPreferences.getString("authenticate_user", ""));

            if (authUser)
            {
                if (sharedPreferences.contains("type_user") && sharedPreferences.contains("authenticate_user"))
                {
                    String getTypeUser = sharedPreferences.getString("type_user", "");

                    switch (getTypeUser)
                    {
                        case "Pet Owner":
                        case "Student":
                        case "Veterinary":
                        case "ONG":
                            startActivity(new Intent(activity, FeedActivity.class));
                            overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                            finish();
                            break;
                    }
                }
            }
            else
            {
                createDialogLoginOffline();
            }
        }
    }

    private void createDialogLoginOffline()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dialog_login_offline));
        builder.setMessage(getString(R.string.message_dialog_login_offline));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.text_ok_alert_login_offline), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkAuthentication()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("authenticate_user"))
        {
            boolean authUser = Boolean.parseBoolean(sharedPreferences.getString("authenticate_user", ""));

            if (userFirebase != null)
            {
                if (authUser)
                {
                    openActivitySplash();

                    String id = EncryptionSHA1.encryptionString(userFirebase.getEmail());

                    DatabaseReference accountsRef = ServicesFirebase.getFirebaseDatabase().child("registered_users").child(Objects.requireNonNull(id));

                    accountsRef.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            UserClass userClass = dataSnapshot.getValue(UserClass.class);

                            typeAccount = Objects.requireNonNull(userClass).getTypeUser();

                            switch (typeAccount)
                            {
                                case "Pet Owner":
                                case "Student":
                                case "Veterinary":
                                case "ONG":
                                    startActivity(new Intent(activity, FeedActivity.class));
                                    overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                                    finish();
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
                else
                {
                    Intent intent = new Intent(activity, AccountsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private void initializeComponents()
    {
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        constraintLayout = findViewById(R.id.constraintLayoutLogin);

        initializeGoogleSign();
    }

    private void initializeGoogleSign()
    {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.id_google_cloud_client)).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(activity).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }

    private void createDialogLoading()
    {
        AlertDialog.Builder alertLoading = new AlertDialog.Builder(this);

        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.loading, null);

        TextView textViewAlertLoading = view.findViewById(R.id.textViewTitleLoading);
        textViewAlertLoading.setVisibility(View.GONE);

        dialogLoading = alertLoading.create();
        dialogLoading.show();
    }

    private void validateFields()
    {
        if (editTextEmail.getText().toString().equals(""))
        {
            editTextEmail.setError(getString(R.string.exception_email_register_owner));
            MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
        }
        else if (editTextPassword.getText().toString().equals(""))
        {
            editTextPassword.setError(getString(R.string.exception_password_register_owner));
            MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
        }
        else
        {
            check = checkConection();

            if (checkConection())
            {
                authenticateUser();
            }
            else
            {
                createSnackBar();
            }
        }
    }

    private void createSnackBar()
    {
        Snackbar.make(constraintLayout, R.string.text_snack_bar_login_check_internet, Snackbar.LENGTH_LONG).show();
    }

    private void disableObjects()
    {
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
    }

    private void enableObjects()
    {
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
    }

    private void authenticateUser()
    {
        createDialogLoading();
        auth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    disableObjects();
                    checkAccountFirebase(editTextEmail.getText().toString(), "Email");
                }
                else
                {
                    dialogLoading.cancel();
                    enableObjects();
                    try
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch (FirebaseAuthInvalidUserException e)
                    {
                        exception = getString(R.string.exception_login_user_not_exist);
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        exception = getString(R.string.exception_email_password_login);
                    }
                    catch (Exception e)
                    {
                        exception = getString(R.string.error_login) + e.getMessage();
                        e.printStackTrace();
                    }
                    MessagesToast.createMessageError(exception, activity);
                }
            }
        });
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

    private void checkAccountFirebase(String emailUser, final String provider)
    {
        try
        {
            String encryptionEmail = EncryptionSHA1.encryptionString(emailUser);

            DatabaseReference usuarioRef = ServicesFirebase.getFirebaseDatabase().child("registered_users").child(Objects.requireNonNull(encryptionEmail));

            usuarioRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    UserClass userClass = dataSnapshot.getValue(UserClass.class);

                    if (provider.equals("Email"))
                    {
                        getTypeAccount = Objects.requireNonNull(userClass).getTypeUser();
                        updateAuthPreferences();

                        switch (getTypeAccount)
                        {
                            case "Pet Owner":
                            case "Student":
                            case "Veterinary":
                            case "ONG":
                                startActivity(new Intent(activity, FeedActivity.class));
                                overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                                finish();
                                break;
                        }
                    }
                    else
                    {
                        try
                        {
                            dialogLoading.cancel();
                            getTypeAccount = Objects.requireNonNull(userClass).getTypeUser();

                            switch (getTypeAccount)
                            {
                                case "Pet Owner":
                                case "Student":
                                case "Veterinary":
                                case "ONG":
                                    startActivity(new Intent(activity, FeedActivity.class));
                                    overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                                    finish();
                                    break;
                            }
                        }
                        catch (NullPointerException e)
                        {
                            Intent intent = new Intent(activity, ChooseActivity.class);
                            intent.putExtra("getName", nameGoogle);
                            intent.putExtra("getEmail", emailGoogle);
                            intent.putExtra("getProvedor", provider);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    MessagesToast.createMessageError(getString(R.string.exception_get_type_account), activity);
                }
            });
        }
        catch (Exception e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_acess_firebase_class_get_type_account) + e, activity);
        }
    }

    private void handleResult(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();

            nameGoogle = Objects.requireNonNull(account).getDisplayName();
            emailGoogle = Objects.requireNonNull(account).getEmail();
            String providerGoogle = "Google";

            createDialogLoading();
            disableObjects();
            checkAccountFirebase(emailGoogle, providerGoogle);
        }
        else
        {
            MessagesToast.createMessageError(getString(R.string.exception_signIn_google) + result, activity);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                check = checkConection();

                if (checkConection())
                {
                    if (task.isSuccessful())
                    {
                        handleResult(result);
                    }
                    else
                    {
                        MessagesToast.createMessageError(getString(R.string.exception_auth_google_signIn) + task, activity);
                    }
                }
                else
                {
                    createSnackBar();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN)
        {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            }
            else
            {
                MessagesToast.createMessageError(getString(R.string.exception_connect_servidor_google) + result, activity);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        MessagesToast.createMessageError(getString(R.string.exception_connect_servidor_google) + connectionResult, activity);
    }

    public void loginGoogle(View view)
    {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, GOOGLE_SIGN);
    }

    public void loginEmail(View view)
    {
        validateFields();
    }

    public void userType(View view)
    {
        startActivity(new Intent(this, ChooseActivity.class));
        finish();
    }

    public void forgotPassword(View view)
    {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}
