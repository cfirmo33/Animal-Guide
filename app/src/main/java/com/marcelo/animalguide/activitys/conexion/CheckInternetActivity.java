package com.marcelo.animalguide.activitys.conexion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.marcelo.animalguide.R;

import java.util.Objects;

public class CheckInternetActivity extends AppCompatActivity
{
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);
    }

    public boolean verificarConex()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();

        boolean conex;
        if (netInfo != null && netInfo.isConnected())
        {
            conex = true;
            finish();
        }
        else
        {
            conex = false;
            finish();
            startActivity(new Intent(activity, CheckInternetActivity.class));
        }

        return conex;
    }

    public void verificarConex(View view)
    {
        verificarConex();
    }
}
