package com.marcelo.animalguide.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Marcelo Caregnato Cordeiro de Souza 12/07/2019 00:05
 * Class responsible for granting app access to camera and gallery of mobile device
 */

public class PermissionsPhotos
{
    public static boolean validatePermissions(String[] permissions, Activity activity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            List<String> listPermissions = new ArrayList<>();

            for (String permission : permissions)
            {
                Boolean isAllowed = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;

                if (!isAllowed)
                {
                    listPermissions.add(permission);
                }
            }

            if (listPermissions.isEmpty())
            {
                return true;
            }

            String[] newPermissions = new String[listPermissions.size()];
            listPermissions.toArray(newPermissions);

            ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        }

        return true;
    }
}
