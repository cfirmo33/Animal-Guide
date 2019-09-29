package com.marcelo.animalguide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.marcelo.animalguide.R;

public class FeedFragment extends Fragment
{
    private View view;


    public FeedFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_feed, container, false);


        return view;
    }


}
