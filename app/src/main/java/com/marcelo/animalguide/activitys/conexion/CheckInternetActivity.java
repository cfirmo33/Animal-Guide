package com.marcelo.animalguide.activitys.conexion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.marcelo.animalguide.R;

import java.util.Objects;

public class CheckInternetActivity extends AppCompatActivity
{
    private Activity activity = this;
    private ConnectivityManager cm;
    private NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);
    }

    private void verificarConex()
    {
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected())
        {
            finish();
        }
        else
        {
            startActivity(new Intent(activity, CheckInternetActivity.class));
            finish();
        }
    }

    public void verificarConex(View view)
    {
        verificarConex();
    }
}
