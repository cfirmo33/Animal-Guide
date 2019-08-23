package com.marcelo.animalguide.registers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.MainActivity;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.firebase.UserFirebase;
import com.marcelo.animalguide.models.classes.UserGoogle;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.marcelo.animalguide.models.classes.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterVeterinaryActivity extends AppCompatActivity
{
    private EditText editTextNameUser, editTextEmailUser, editTextPasswordUser;
    private TextInputLayout inputTextPasswordRegisterVeterinary;
    private CircleImageView circleImageViewVeterinario;
    private Button btnRegisterVeterinario;
    private AlertDialog dialog, dialogPhotos;

    private UserClass userClass = new UserClass();
    private UserGoogle userGoogle = new UserGoogle();
    private FirebaseAuth authentication = ServicesFirebase.getFirebaseAuth();
    private StorageReference imageReference = ServicesFirebase.getFirebaseStorage();

    private Activity activity = this;
    private Bitmap imagem;
    private String message;
    private String idUser;
    private String exception;
    private String getNome;
    private String getEmail;
    private String getProvedor;
    private String typeUser;
    private String accountGoogle = "Sim";
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_veterinary);

        initializeObjects();
        getDataBundle();
    }

    public void initializeObjects()
    {
        inputTextPasswordRegisterVeterinary = findViewById(R.id.inputTextPasswordRegisterVeterinary);
        editTextNameUser = findViewById(R.id.editTextNameLastNameVeterinary);
        editTextEmailUser = findViewById(R.id.editTextEmailRegisterVeterinary);
        editTextPasswordUser = findViewById(R.id.editTextPasswordRegisterVeterinary);
        circleImageViewVeterinario = findViewById(R.id.imageUserVeterinary);
        btnRegisterVeterinario = findViewById(R.id.buttonRegisterVeterinary);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(getString(R.string.title_toolbar_veterinary));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void getDataBundle()
    {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            getProvedor = bundle.getString("getProvedor");

            if (Objects.equals(getProvedor, "Google"))
            {
                accountGoogle = "Sim";

                inputTextPasswordRegisterVeterinary.setVisibility(View.GONE);
                getNome = bundle.getString("getName");
                getEmail = bundle.getString("getEmail");

                editTextNameUser.setText(getNome);
                editTextEmailUser.setText(getEmail);
                editTextNameUser.setSelection(editTextNameUser.length());
            }
        }
    }

    public void registerVeterinary(View view)
    {
        validateFields();
    }

    public void validateFields()
    {
        if (accountGoogle.equals("Não"))
        {
            if (editTextNameUser.getText().toString().equals(""))
            {
                editTextNameUser.setError(getString(R.string.exception_name_last_name_register_owner));
                MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
            }
            else if (editTextEmailUser.getText().toString().equals(""))
            {
                editTextEmailUser.setError(getString(R.string.exception_email_register_owner));
                MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
            }
            else if (editTextPasswordUser.getText().toString().equals(""))
            {
                editTextPasswordUser.setError(getString(R.string.exception_password_register_owner));
                MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
            }
            else
            {
                userClass = new UserClass();
                userClass.setName(editTextNameUser.getText().toString());
                userClass.setEmail(editTextEmailUser.getText().toString());
                userClass.setPassword(editTextPasswordUser.getText().toString());
                userClass.setProvedor("Email");
                if (imagem == null)
                {
                    createAlertDialog();
                }
                else
                {
                    registerUserEmailAndPassword();
                }
            }
        }
        else
        {
            if (editTextNameUser.getText().toString().equals(""))
            {
                editTextNameUser.setError(getString(R.string.exception_name_last_name_register_owner));
                MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
            }
            else if (editTextEmailUser.getText().toString().equals(""))
            {
                editTextEmailUser.setError(getString(R.string.exception_email_register_owner));
                MessagesToast.createMessageWarning(getString(R.string.message_toast_exception_register_owner), this);
            }
            else
            {
                typeUser = "Veterinary";
                userGoogle.setTypeUser(typeUser);
                userGoogle.setIdUser(getEmail);
                userGoogle.setNameGoogle(getNome);
                userGoogle.setEmailGoogle(getEmail);
                userGoogle.setProvedor(getProvedor);

                if (imagem == null)
                {
                    createAlertDialog();
                }
                else
                {
                    createDialogLoading();
                    saveImageFirebaseStorage();
                }
            }
        }
    }

    public void createAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_alert_dialog_insert_photo_register));
        builder.setMessage(getString(R.string.message_alert_dialog_insert_photo_register));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.button_positive_alert_dialog_insert_photo_register), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (accountGoogle.equals("Sim"))
                {
                    userGoogle.saveDatabase("registered_users");
                    startActivity(new Intent(activity, MainActivity.class));
                    finish();
                }
                else
                {
                    registerUserEmailAndPassword();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.button_negative_alert_dialog_insert_photo_register), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createDialogLoading()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setView(R.layout.loading);

        dialog = alert.create();
        dialog.show();
    }

    public void saveImageFirebaseStorage()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 80, baos);
        byte[] dadosImagem = baos.toByteArray();

        idUser = EncryptionSHA1.encryptionString(editTextEmailUser.getText().toString());

        /* Structure of photos in FirebaseStorage **/
        StorageReference imagemRef = imageReference
                .child("Cadastro do Usuário")
                .child(idUser)
                .child("photo.png");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                message = getString(R.string.error_save_photo_firebase_register) + e;

                MessagesToast.createMessageError(message, activity);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot)
            {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        imageReference = imageReference
                                .child("Cadastro do Usuário")
                                .child(idUser)
                                .child("photo.png");

                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                if (accountGoogle.equals("Não"))
                                {
                                    dialog.cancel();
                                    UserFirebase.updateNameUser(userClass.getName());
                                    updatePhotoUser(uri);
                                    saveDatabaseAccountEmail();
                                }
                                else
                                {
                                    dialog.cancel();
                                    userGoogle.saveDatabase("registered_users");
                                    startActivity(new Intent(activity, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void updatePhotoUser(Uri url)
    {
        UserFirebase.updatePhotoUser(url);
    }

    public void registerUserEmailAndPassword()
    {
        createDialogLoading();

        authentication.createUserWithEmailAndPassword(userClass.getEmail(), userClass.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    disableObjects();

                    if (imagem != null)
                    {
                        saveImageFirebaseStorage();
                    }

                    saveDatabaseAccountEmail();

                    UserFirebase.updateNameUser(userClass.getName());
                }
                else
                {
                    dialog.cancel();
                    try
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch (FirebaseAuthWeakPasswordException e)
                    {
                        exception = getString(R.string.exception_password_register);
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        exception = getString(R.string.exception_email_register);
                    }
                    catch (FirebaseAuthUserCollisionException e)
                    {
                        exception = getString(R.string.exception_email_exists_register);
                    }

                    catch (Exception e)
                    {
                        exception = getString(R.string.exception_register_user) + e.getMessage();
                        e.printStackTrace();
                    }
                    MessagesToast.createMessageError(exception, activity);
                }
            }
        });
    }

    public void saveDatabaseAccountEmail()
    {
        typeUser = "Veterinary";
        userClass.setTypeUser(typeUser);
        userClass.setIdUser(userClass.getEmail());
        try
        {
            dialog.cancel();
            userClass.saveDatabase("registered_users");
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }
        catch (Exception e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_save_user_owner_database) + e, activity);
        }
    }

    public void disableObjects()
    {
        btnRegisterVeterinario.setEnabled(false);
        editTextNameUser.setEnabled(false);
        editTextEmailUser.setEnabled(false);
        editTextPasswordUser.setEnabled(false);
    }

    public void openCameraVeterinario(View view)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_customized_photos, null);
        Button btnCamera = alertView.findViewById(R.id.buttonCameraDialog);
        Button btnGaleria = alertView.findViewById(R.id.buttonGaleriaDialog);
        Button btnCancel = alertView.findViewById(R.id.buttonCancelDialog);

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogPhotos.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openDeviceCamera();
            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openDeviceGallery();
            }
        });

        alertDialogBuilder.setView(alertView);
        dialogPhotos = alertDialogBuilder.create();
        dialogPhotos.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPhotos.show();
    }

    public void openDeviceGallery()
    {
        Intent takeGalleryIntent = new Intent();
        takeGalleryIntent.setType("image/*");
        takeGalleryIntent.setAction(Intent.ACTION_PICK);
        dialogPhotos.cancel();
        startActivityForResult(takeGalleryIntent, SELECAO_GALERIA);
    }

    public void openDeviceCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            dialogPhotos.cancel();
            startActivityForResult(takePictureIntent, SELECAO_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            try
            {
                if (requestCode == SELECAO_CAMERA)
                {
                    Bundle extras = data.getExtras();
                    imagem = (Bitmap) Objects.requireNonNull(extras).get("data");
                    circleImageViewVeterinario.setImageBitmap(imagem);
                    MessagesToast.createMessageSucess(getString(R.string.sucess_insert_photo_camera), activity);
                }
                else if (requestCode == SELECAO_GALERIA)
                {
                    InputStream inputStream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                    imagem = BitmapFactory.decodeStream(inputStream);
                    circleImageViewVeterinario.setImageBitmap(imagem);
                    MessagesToast.createMessageSucess(getString(R.string.sucess_insert_photo_camera), activity);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
