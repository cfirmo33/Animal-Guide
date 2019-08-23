package com.marcelo.animalguide.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.permissions.PermissionsPhotos;
import com.marcelo.animalguide.registers.RegisterONGActivity;
import com.marcelo.animalguide.registers.RegisterOwnerActivity;
import com.marcelo.animalguide.registers.RegisterStudentActivity;
import com.marcelo.animalguide.registers.RegisterVeterinaryActivity;

public class ChooseActivity extends AppCompatActivity
{
    private String[] permissionsRequired = new String[]
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
            };
    private String getNome, getEmail, getProvedor;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        activatePermissions();
        getDataBundle();
    }

    private void activatePermissions()
    {
        PermissionsPhotos.validatePermissions(permissionsRequired, this, 1);
    }

    private void getDataBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            getNome = bundle.getString("getName");
            getEmail = bundle.getString("getEmail");
            getProvedor = bundle.getString("getProvedor");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionResult : grantResults)
        {
            if (permissionResult == PackageManager.PERMISSION_DENIED)
            {
                alertUserPermission();
            }
        }
    }

    private void alertUserPermission()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_alert_permissions_register_owner));
        builder.setMessage(getString(R.string.message_alert_register_owner));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.button_confirm_alert_register_owner), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void registerOwner(View view)
    {
        Intent intent = new Intent(activity, RegisterOwnerActivity.class);
        intent.putExtra("getName", getNome);
        intent.putExtra("getEmail", getEmail);
        intent.putExtra("getProvedor", getProvedor);

        startActivity(intent);
    }

    public void registerStudent(View view)
    {
        Intent intent = new Intent(activity, RegisterStudentActivity.class);
        intent.putExtra("getName", getNome);
        intent.putExtra("getEmail", getEmail);
        intent.putExtra("getProvedor", getProvedor);

        startActivity(intent);
    }

    public void registerVeterinary(View view)
    {
        Intent intent = new Intent(activity, RegisterVeterinaryActivity.class);
        intent.putExtra("getName", getNome);
        intent.putExtra("getEmail", getEmail);
        intent.putExtra("getProvedor", getProvedor);

        startActivity(intent);
    }

    public void registerONG(View view)
    {
        Intent intent = new Intent(activity, RegisterONGActivity.class);
        intent.putExtra("getName", getNome);
        intent.putExtra("getEmail", getEmail);
        intent.putExtra("getProvedor", getProvedor);

        startActivity(intent);
    }
}
