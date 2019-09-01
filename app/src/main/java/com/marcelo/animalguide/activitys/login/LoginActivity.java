package com.marcelo.animalguide.activitys.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.conexion.CheckInternetActivity;
import com.marcelo.animalguide.activitys.main_activitys.ONGMainActivity;
import com.marcelo.animalguide.activitys.main_activitys.OwnerMainActivity;
import com.marcelo.animalguide.activitys.main_activitys.StudentMainActivity;
import com.marcelo.animalguide.activitys.main_activitys.VeterinaryMainActivity;
import com.marcelo.animalguide.activitys.password.ForgotPasswordActivity;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private static final int GOOGLE_SIGN = 300;
    private Activity activity = this;
    private String exception;
    private String nameGoogle, emailGoogle, getTypeAccount;
    private Boolean check;

    private EditText editTextEmail, editTextPassword;
    private AlertDialog dialogLoading;
    private ConstraintLayout constraintLayout;

    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private GoogleApiClient googleApiClient;
    private GoogleSignInResult result;

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
        }
        else
        {
            getStatusInternet = false;
        }

        return getStatusInternet;
    }

    private void initializeComponents()
    {
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        constraintLayout = findViewById(R.id.constraintLayoutLogin);

        check = checkConection();

        if (!check)
        {
            startActivity(new Intent(activity, CheckInternetActivity.class));
            finish();
        }
        else
        {
            initializeGoogleSign();
        }
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
        textViewAlertLoading.setText(getString(R.string.text_view_alert_dialog_login));

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
        Snackbar.make(constraintLayout, R.string.text_snack_bar_login_check_internet,
                Snackbar.LENGTH_LONG)
                .show();
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
        auth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    createDialogLoading();
                    disableObjects();
                    checkAccountFirebase(editTextEmail.getText().toString(), "Email");
                }
                else
                {
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
                        dialogLoading.cancel();
                        getTypeAccount = Objects.requireNonNull(userClass).getTypeUser();

                        switch (getTypeAccount)
                        {
                            case "Pet Owner":
                                startActivity(new Intent(activity, OwnerMainActivity.class));
                                finish();
                                break;
                            case "Student":
                                startActivity(new Intent(activity, StudentMainActivity.class));
                                finish();
                                break;
                            case "Veterinary":
                                startActivity(new Intent(activity, VeterinaryMainActivity.class));
                                finish();
                                break;
                            case "ONG":
                                startActivity(new Intent(activity, ONGMainActivity.class));
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
                                    startActivity(new Intent(activity, OwnerMainActivity.class));
                                    finish();
                                    break;
                                case "Student":
                                    startActivity(new Intent(activity, StudentMainActivity.class));
                                    finish();
                                    break;
                                case "Veterinary":
                                    startActivity(new Intent(activity, VeterinaryMainActivity.class));
                                    finish();
                                    break;
                                case "ONG":
                                    startActivity(new Intent(activity, ONGMainActivity.class));
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
    }

    public void forgotPassword(View view)
    {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}
