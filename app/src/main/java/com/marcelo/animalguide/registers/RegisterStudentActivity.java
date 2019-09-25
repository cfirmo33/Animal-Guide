package com.marcelo.animalguide.registers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.main_activitys.StudentMainActivity;
import com.marcelo.animalguide.encryption.Base64Custom;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.firebase.UserFirebase;
import com.marcelo.animalguide.models.classes.BackupSharedPreferences;
import com.marcelo.animalguide.models.classes.DatesCustomized;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RegisterStudentActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks
{
    private EditText editTextNameUser, editTextEmailUser, editTextPasswordUser;
    private ConstraintLayout constraintLayout;
    private TextInputLayout inputTextPasswordRegisterStudent;
    private CircleImageView circleImageViewStudent;
    private Button btnRegisterStudent;
    private AlertDialog dialog, dialogPhotos, dialogSave, dialogPermissions;

    private UserClass userClass = new UserClass();
    private BackupSharedPreferences backupSharedPreferences = new BackupSharedPreferences();
    private FirebaseAuth authentication = ServicesFirebase.getFirebaseAuth();
    private DatabaseReference firebaseRef = ServicesFirebase.getFirebaseDatabase();
    private DatabaseReference userRef = ServicesFirebase.getFirebaseDatabase();
    private StorageReference imageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;

    private Activity activity = this;
    private Bitmap imagem;
    private String message, idUser, exception, getNome, getEmail, getProvedor, typeUser, accountGoogle = "Não", getPasswordEncrypted, getPhotoPreferences;
    private Boolean check, checkPreference = false;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";
    private String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

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
        setContentView(R.layout.activity_register_student);

        initializeObjects();
        getDataBundle();
    }

    public void initializeObjects()
    {
        constraintLayout = findViewById(R.id.constraintLayoutStudent);
        inputTextPasswordRegisterStudent = findViewById(R.id.inputTextPasswordRegisterStudent);
        editTextNameUser = findViewById(R.id.editTextNameLastNameStudent);
        editTextEmailUser = findViewById(R.id.editTextEmailRegisterStudent);
        editTextPasswordUser = findViewById(R.id.editTextPasswordRegisterStudent);
        circleImageViewStudent = findViewById(R.id.imageUserStudent);
        btnRegisterStudent = findViewById(R.id.buttonRegisterStudent);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(getString(R.string.title_toolbar_student));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

    private void getDataBundle()
    {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            getProvedor = bundle.getString("getProvedor");

            if (Objects.equals(getProvedor, "Google"))
            {
                accountGoogle = "Sim";

                inputTextPasswordRegisterStudent.setVisibility(View.GONE);
                getNome = bundle.getString("getName");
                getEmail = bundle.getString("getEmail");

                editTextNameUser.setText(getNome);
                editTextEmailUser.setText(getEmail);
                editTextNameUser.setSelection(editTextNameUser.length());
            }
        }
    }

    public void registerStudent(View view)
    {
        validadeFields();
    }

    public void validadeFields()
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
                check = checkConection();

                if (checkConection())
                {
                    userClass.setName(editTextNameUser.getText().toString());
                    userClass.setEmail(editTextEmailUser.getText().toString());
                    userClass.setPassword(editTextPasswordUser.getText().toString());
                    getPasswordEncrypted = Base64Custom.encryption(editTextPasswordUser.getText().toString());
                    userClass.setProvedor("Email");
                    userClass.setSaveLogin(false);
                    if (imagem == null)
                    {
                        createAlertDialog();
                    }
                    else
                    {
                        registerUserEmailAndPassword();
                    }
                }
                else
                {
                    createSnackBar();
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
                check = checkConection();

                if (checkConection())
                {
                    typeUser = "Student";
                    userClass.setTypeUser(typeUser);
                    userClass.setIdUser(getEmail);
                    userClass.setName(getNome);
                    userClass.setEmail(getEmail);
                    userClass.setProvedor(getProvedor);
                    userClass.setSaveLogin(false);

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
                else
                {
                    createSnackBar();
                }
            }
        }
    }

    private void createSnackBar()
    {
        Snackbar.make(constraintLayout, R.string.text_snack_bar_register_check_internet, Snackbar.LENGTH_LONG).show();
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
                    createDialogLoading();
                    createAlertDialogSaveLogin();
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

    private void createAlertDialogSaveLogin()
    {
        if (accountGoogle.equals("Não"))
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
                    checkPreference = true;
                    saveSharedPreferenceDados();
                }
            });
            builder.setNegativeButton(getString(R.string.btn_not_save_password), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    saveSharedPreferenceDados();
                }
            });

            dialogSave = builder.create();
            dialogSave.show();
        }

        else if (accountGoogle.equals("Sim"))
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
                    userClass.saveDatabase("registered_users");

                    checkPreference = true;
                    saveSharedPreferenceDados();
                }
            });
            builder.setNegativeButton(getString(R.string.btn_not_save_password), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    userClass.saveDatabase("registered_users");

                    saveSharedPreferenceDados();
                }
            });

            dialogSave = builder.create();
            dialogSave.show();
        }
    }

    private void createAlertDialogPermissionsApp()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_permissions_app, null);
        TextView textNotPermission = alertView.findViewById(R.id.textViewNotPermission);
        TextView textOpenSettings = alertView.findViewById(R.id.textViewOpenSettingsPermissions);

        textNotPermission.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MessagesToast.createMessageWarning(getString(R.string.permission_negada), activity);
                dialogPermissions.cancel();
            }
        });

        textOpenSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setView(alertView);
        dialogPermissions = alertDialogBuilder.create();
        dialogPermissions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPermissions.show();
    }

    @SuppressLint("ApplySharedPref")
    private void saveSharedPreferenceDados()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        if (checkPreference)
        {
            try
            {
                if (accountGoogle.equals("Não"))
                {
                    editor.putString("authenticate_user", String.valueOf(true));
                    editor.putString("nome_user_menu", editTextNameUser.getText().toString());
                    editor.putString("provedor_user", "Email");
                    editor.putString("type_user", "Student");
                    editor.putString("path_foto_user_menu", getPhotoPreferences);
                    editor.putString("email_user", editTextEmailUser.getText().toString());
                    editor.putString("password_criptografado_base64", getPasswordEncrypted);
                    editor.commit();

                    backupSharedPreferences.setNameUser(editTextNameUser.getText().toString());
                    backupSharedPreferences.setTypeUser("Student");
                    backupSharedPreferences.setEmailUser(editTextEmailUser.getText().toString());
                    backupSharedPreferences.setPasswordUser(getPasswordEncrypted);
                    backupSharedPreferences.setPathFoto(getPhotoPreferences);
                    backupSharedPreferences.setProvedor("Email");
                    updateStatusDatabase();
                }
                else
                {
                    editor.putString("authenticate_user", String.valueOf(true));
                    editor.putString("nome_user_menu", editTextNameUser.getText().toString());
                    editor.putString("provedor_user", "Google");
                    editor.putString("type_user", "Student");
                    editor.putString("path_foto_user_menu", getPhotoPreferences);
                    editor.putString("email_user", editTextEmailUser.getText().toString());
                    editor.putString("password_criptografado_base64", getPasswordEncrypted);
                    editor.commit();

                    backupSharedPreferences.setNameUser(editTextNameUser.getText().toString());
                    backupSharedPreferences.setTypeUser("Student");
                    backupSharedPreferences.setEmailUser(editTextEmailUser.getText().toString());
                    backupSharedPreferences.setPasswordUser(getPasswordEncrypted);
                    backupSharedPreferences.setPathFoto(getPhotoPreferences);
                    backupSharedPreferences.setProvedor("Google");
                    updateStatusDatabase();
                }
            }
            catch (Exception e)
            {
                MessagesToast.createMessageError(getString(R.string.exception_save_password_shared_preference) + e, activity);
            }
        }
        else if (!checkPreference)
        {
            try
            {
                if (accountGoogle.equals("Não"))
                {
                    editor.putString("authenticate_user", String.valueOf(true));
                    editor.putString("nome_user_menu", editTextNameUser.getText().toString());
                    editor.putString("provedor_user", "Email");
                    editor.putString("type_user", "Student");
                    editor.putString("path_foto_user_menu", getPhotoPreferences);
                    editor.putString("email_user", editTextEmailUser.getText().toString());
                    editor.putString("password_criptografado_base64", getPasswordEncrypted);
                    editor.commit();
                    backupSharedPreferences.setNameUser(editTextNameUser.getText().toString());
                    backupSharedPreferences.setTypeUser("Student");
                    backupSharedPreferences.setEmailUser(editTextEmailUser.getText().toString());
                    backupSharedPreferences.setPasswordUser(getPasswordEncrypted);
                    backupSharedPreferences.setPathFoto(getPhotoPreferences);
                    backupSharedPreferences.setProvedor("Email");

                    backupSharedPreferences.saveDatabase(EncryptionSHA1.encryptionString(editTextEmailUser.getText().toString()));

                    startActivity(new Intent(activity, StudentMainActivity.class));
                    finish();
                }
                else
                {
                    editor.putString("authenticate_user", String.valueOf(true));
                    editor.putString("nome_user_menu", editTextNameUser.getText().toString());
                    editor.putString("provedor_user", "Google");
                    editor.putString("type_user", "Student");
                    editor.putString("path_foto_user_menu", getPhotoPreferences);
                    editor.putString("email_user", editTextEmailUser.getText().toString());
                    editor.putString("password_criptografado_base64", getPasswordEncrypted);
                    editor.commit();
                    backupSharedPreferences.setNameUser(editTextNameUser.getText().toString());
                    backupSharedPreferences.setTypeUser("Student");
                    backupSharedPreferences.setEmailUser(editTextEmailUser.getText().toString());
                    backupSharedPreferences.setPasswordUser(getPasswordEncrypted);
                    backupSharedPreferences.setPathFoto(getPhotoPreferences);
                    backupSharedPreferences.setProvedor("Google");

                    backupSharedPreferences.saveDatabase(EncryptionSHA1.encryptionString(editTextEmailUser.getText().toString()));

                    startActivity(new Intent(activity, StudentMainActivity.class));
                    finish();
                }
            }
            catch (Exception e)
            {
                MessagesToast.createMessageError(getString(R.string.exception_save_password_shared_preference) + e, activity);
            }
        }
    }

    private void updateStatusDatabase()
    {
        String idDatabase = EncryptionSHA1.encryptionString(editTextEmailUser.getText().toString());

        try
        {
            dialog.cancel();
            userRef = firebaseRef.child("registered_users").child(Objects.requireNonNull(idDatabase));

            userRef.child("saveLogin").setValue(true);

            String getDateCurrent = DatesCustomized.getData();
            backupSharedPreferences.saveDatabase(DatesCustomized.dateCustom(getDateCurrent));

            startActivity(new Intent(activity, StudentMainActivity.class));
            finish();
        }
        catch (Exception e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_update_status_dados_login) + e, activity);
        }
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
        circleImageViewStudent.setDrawingCacheEnabled(true);
        circleImageViewStudent.buildDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 80, baos);
        byte[] dadosImagem = baos.toByteArray();

        idUser = EncryptionSHA1.encryptionString(editTextEmailUser.getText().toString());

        /* Structure of photos in FirebaseStorage **/
        StorageReference imagemRef = imageReference.child("Cadastro do Usuário").child(idUser).child("photo.png");

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
                        imageReference = imageReference.child("Cadastro do Usuário").child(idUser).child("photo.png");

                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                getPhotoPreferences = uri.toString();

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
                                    userClass.saveDatabase("registered_users");
                                    startActivity(new Intent(activity, StudentMainActivity.class));
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
                    else
                    {
                        saveDatabaseAccountEmail();
                    }

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
        typeUser = "Student";
        userClass.setTypeUser(typeUser);
        userClass.setIdUser(userClass.getEmail());
        try
        {
            dialog.cancel();
            userClass.saveDatabase("registered_users");
            createAlertDialogSaveLogin();
        }
        catch (Exception e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_save_user_owner_database) + e, activity);
        }
    }

    public void disableObjects()
    {
        btnRegisterStudent.setEnabled(false);
        editTextNameUser.setEnabled(false);
        editTextEmailUser.setEnabled(false);
        editTextPasswordUser.setEnabled(false);
    }

    public void openCameraStudent(View view)
    {
        if (EasyPermissions.hasPermissions(activity, permissionsRequired))
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
        else
        {
            EasyPermissions.requestPermissions(activity, getString(R.string.message_alert_register_owner), 1, permissionsRequired);
        }
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
                    circleImageViewStudent.setImageBitmap(imagem);
                    MessagesToast.createMessageSucess(getString(R.string.sucess_insert_photo_camera), activity);
                }
                else if (requestCode == SELECAO_GALERIA)
                {
                    InputStream inputStream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                    imagem = BitmapFactory.decodeStream(inputStream);
                    circleImageViewStudent.setImageBitmap(imagem);
                    MessagesToast.createMessageSucess(getString(R.string.sucess_insert_photo_camera), activity);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        {
            //Lib EasyPermissions interface takes action if this is true.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, activity);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms)
    {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms)
    {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            createAlertDialogPermissionsApp();
        }
    }
}
