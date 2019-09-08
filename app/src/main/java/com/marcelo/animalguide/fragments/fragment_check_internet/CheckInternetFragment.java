package com.marcelo.animalguide.fragments.fragment_check_internet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.conexion.CheckInternetActivity;

import java.util.Objects;

public class CheckInternetFragment extends Fragment
{
    private View view;
    private Activity activity = getActivity();
    private Button btnCheckInternet;

    public CheckInternetFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_check_internet, container, false);

        initializeObjects();
        clickButton();

        return view;
    }

    private void initializeObjects()
    {
        btnCheckInternet = view.findViewById(R.id.buttonCheckInternetFragment);
    }

    private void clickButton()
    {
        btnCheckInternet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                verificarConex();
            }
        });
    }

    private void verificarConex()
    {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected())
        {
            Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
        }
        else
        {
            Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
            startActivity(new Intent(activity, CheckInternetActivity.class));
        }
    }
}
