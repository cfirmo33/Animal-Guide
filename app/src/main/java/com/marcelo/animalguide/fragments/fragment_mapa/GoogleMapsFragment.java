package com.marcelo.animalguide.fragments.fragment_mapa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelo.animalguide.R;

public class GoogleMapsFragment extends Fragment
{
    private View view;

    public GoogleMapsFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_google_maps, container, false);



        return view;
    }
}
