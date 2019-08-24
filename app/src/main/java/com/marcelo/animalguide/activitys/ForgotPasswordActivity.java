package com.marcelo.animalguide.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.message_toast.MessagesToast;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText editTextEmail;

    private Activity activity = this;

    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initializeObjects();
    }

    private void initializeObjects()
    {
        editTextEmail = findViewById(R.id.editTextEmailForgotPassword);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(getString(R.string.title_toolbar_forgot_password));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void validadeFields()
    {
        if (editTextEmail.getText().toString().equals(""))
        {
            editTextEmail.setError(getString(R.string.text_forgot_password_empty));
            MessagesToast.createMessageError(getString(R.string.message_toast_forgot_password), activity);
        }
        else
        {
            createAlertDialog(editTextEmail.getText().toString());
        }
    }

    private void createAlertDialog(String email)
    {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(R.string.title_alert_forgot_password);
                            builder.setMessage(getString(R.string.message_alert_forgot_password));
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    editTextEmail.setText("");
                                    finish();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else
                        {
                            MessagesToast.createMessageError(getString(R.string.messase_toast_exception_forgot_password)
                                    + task, activity);
                        }
                    }
                });
    }

    public void recoveryPassword(View view)
    {
        validadeFields();
    }
}
