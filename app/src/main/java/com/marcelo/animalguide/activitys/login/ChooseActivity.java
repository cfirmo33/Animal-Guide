package com.marcelo.animalguide.activitys.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.registers.RegisterONGActivity;
import com.marcelo.animalguide.registers.RegisterOwnerActivity;
import com.marcelo.animalguide.registers.RegisterStudentActivity;
import com.marcelo.animalguide.registers.RegisterVeterinaryActivity;

public class ChooseActivity extends AppCompatActivity
{
    private String getNome, getEmail, getProvedor;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        getDataBundle();
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
